package ru.bratusev.smartlab.feature_customScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.bratusev.smartlab.domain.core.model.CustomWidget
import ru.bratusev.smartlab.domain.core.model.socket.ServiceEntity
import ru.bratusev.smartlab.domain.core.usecase.GetCustomWidgetsUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetLoggerUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetServiceEntitiesUseCase
import ru.bratusev.smartlab.domain.core.usecase.ObserveSocketErrorsUseCase
import ru.bratusev.smartlab.domain.core.usecase.SetCustomWidgetsUseCase
import ru.bratusev.smartlab.domain.core.usecase.UpdateSensorUseCase
import ru.bratusev.smartlab.feature_customScreen.models.CustomScreenState
import ru.bratusev.smartlab.feature_customScreen.models.Event

class CustomScreenViewModel(
    private val saveWidgets: SetCustomWidgetsUseCase,
    private val logger: GetLoggerUseCase,
    private val updateSensorUseCase: UpdateSensorUseCase,
    getWidgets: GetCustomWidgetsUseCase,
    getServiceEntitiesUseCase: GetServiceEntitiesUseCase,
    observeSocketErrorsUseCase: ObserveSocketErrorsUseCase,

    ) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomScreenState())
    val uiState: StateFlow<CustomScreenState> = _uiState

    init {
        getServiceEntitiesUseCase.invoke().onEach {
            onServiceEntitiesUpdated(it)
        }.launchIn(viewModelScope)

        observeSocketErrorsUseCase.invoke().onEach { errors ->
            if (errors.isNotEmpty()) {
                val messages = errors.map { it.message ?: it.toString() }
                updateState(uiState.value.copy(socketErrors = messages))
            }
        }.launchIn(viewModelScope)

        getWidgets().onEach { result ->
            result.onSuccess {
                val resultWidgets = it.ifEmpty {
                    logger.d(
                        "CustomScreen/init", "Custom widgets store is empty. Creating new."
                    )
                    getWidgetsPreview()
                }
                updateState(_uiState.value.copy(widgets = resultWidgets))
            }
            result.onFailure { error ->
                updateState(_uiState.value.copy(widgets = getWidgetsPreview()))
                logger.e("CustomScreen/init", "Error during getting widgets. Error: $error")
            }
        }.launchIn(viewModelScope)
    }

    private fun onServiceEntitiesUpdated(entities: List<ServiceEntity>) {
        val switches: List<ServiceEntity> = entities.filter { it.domain == "switch" }
        updateState(uiState.value.copy(switchesEntities = switches))
    }

    private fun getWidgetsPreview(): List<CustomWidget> {
        val data = buildList {
            for (k in 0..2) {
                val sensorsId = emptyList<String>().toMutableList()
                add(
                    CustomWidget.SensorsList(
                        sensorsIds = sensorsId, id = k
                    )
                )
            }
        }

        saveWidgets.invoke(data).launchIn(viewModelScope)
        return data
    }

    private fun onCustomButtonClicked() {
        val state = uiState.value.copy(screenName = "New screen name")
        updateState(state)
    }

    private fun onButtonTextUpdated(text: String) {
        val state = uiState.value.copy(screenName = text)
        updateState(state)
    }

    private fun toggleModalMenu() {
        updateState(_uiState.value.copy(isModalOpen = !_uiState.value.isModalOpen))
    }

    private fun updateState(updatedState: CustomScreenState) {
        viewModelScope.launch {
            _uiState.emit(updatedState)
        }
    }

    private fun onSwitchUpdated(switchId: String) {
        uiState.value.switchesEntities.find { it.id == switchId }?.let {
            it.id?.let { id ->
                updateSensorUseCase.invoke(id).onEach { }.launchIn(viewModelScope)
            }
        }
    }

    private fun updateWidget(newState: CustomWidget) {
        logger.d("Updating widgets", "Updating widget with id: ${newState.id}")
        logger.d(
            "Updating widgets",
            "Available widgets ids: ${_uiState.value.widgets.map { it.id }}"
        )
        val updatedWidgets =
            _uiState.value.widgets.map { if (it.id == newState.id) newState else it }
        logger.d("Updating widgets", "New widgets: $updatedWidgets")
        updateState(_uiState.value.copy(widgets = updatedWidgets))
        saveWidgets.invoke(updatedWidgets).launchIn(viewModelScope)
    }

    internal fun handleEvent(event: Event) {
        when (event) {
            Event.OnBackClicked -> Unit
            Event.OnCustomButtonClicked -> onCustomButtonClicked()
            is Event.OnButtonTextUpdated -> onButtonTextUpdated(event.text)
            Event.OnMenuButtonClicked -> toggleModalMenu()
            Event.OnModalCloseClicked -> toggleModalMenu()
            is Event.OnSensorStateChanged -> onSwitchUpdated(
                event.sensorId
            )

            is Event.OnSwitchesWidgetChanged -> updateWidget(
                newState = CustomWidget.SensorsList(
                    sensorsIds = event.chosenIds,
                    id = event.widgetId
                )
            )
        }
    }
}