package ru.bratusev.smartlab.feature_customScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.bratusev.smartlab.domain.core.model.CustomWidget
import ru.bratusev.smartlab.domain.core.model.CustomWidget.SensorsList
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
    private val getWidgetsUseCase: GetCustomWidgetsUseCase,
    private val getServiceEntitiesUseCase: GetServiceEntitiesUseCase,
    observeSocketErrorsUseCase: ObserveSocketErrorsUseCase,

    ) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomScreenState())
    val uiState: StateFlow<CustomScreenState> = _uiState

    private var getServiceEntitiesJob: Job? = null
    private var getWidgetsJob: Job? = null

    init {
        observeSocketErrorsUseCase.invoke().onEach { errors ->
            if (errors.isNotEmpty()) {
                val messages = errors.map { it.message ?: it.toString() }
                updateState(uiState.value.copy(socketErrors = messages))
            }
        }.launchIn(viewModelScope)

        loadData()
    }

    fun loadData() {
        getServiceEntitiesJob?.cancel()
        getWidgetsJob?.cancel()

        getServiceEntitiesJob = getServiceEntitiesUseCase.invoke().onEach {
            onServiceEntitiesUpdated(it)
        }.launchIn(viewModelScope)

        getWidgetsJob = getWidgetsUseCase().onEach { result ->
            result.onSuccess {
                val resultWidgets = it.ifEmpty {
                    emptyList()
                }
                updateState(_uiState.value.copy(widgets = resultWidgets))
            }
            result.onFailure { error ->
                updateState(_uiState.value.copy(widgets = emptyList()))
                logger.e("CustomScreen/loadData", "Error during getting widgets. Error: $error")
            }
        }.launchIn(viewModelScope)
    }

    private fun onServiceEntitiesUpdated(entities: List<ServiceEntity>) {
        val switches: List<ServiceEntity> = entities.filter { it.domain == "switch" }
        updateState(uiState.value.copy(switchesEntities = switches))
    }

    private fun toggleDropDownMenu() {
        updateState(_uiState.value.copy(isDropDownMenuExpanded = !_uiState.value.isDropDownMenuExpanded))
    }

    private fun toggleEditMode() {
        updateState(_uiState.value.copy(isEditMode = !_uiState.value.isEditMode))
    }

    private fun updateState(updatedState: CustomScreenState) {
        viewModelScope.launch {
            _uiState.emit(updatedState)
        }
    }

    private fun updateWidgets(updatedWidgets: List<CustomWidget>) {
        updateState(_uiState.value.copy(widgets = updatedWidgets))
        saveWidgets.invoke(updatedWidgets).onEach { result ->
            result.fold(onSuccess = {
                logger.d("CustomScreenViewModel/updateWidgets", "Successfully saved widgets")
            }, onFailure = { error ->
                logger.e(
                    "CustomScreenViewModel/updateWidget",
                    "Error during saving widgets. Error $error"
                )
            })
        }.launchIn(viewModelScope)
    }

    private fun onSwitchUpdated(switchId: String) {
        updateState(_uiState.value.copy(isUpdating = true))
        uiState.value.switchesEntities.find { it.id == switchId }?.let {
            it.id?.let { id ->
                updateSensorUseCase.invoke(id).onEach { result ->
                    result.fold(onSuccess = {
                        logger.d(
                            "CustomScreenViewModel switch switch",
                            "Switched switch with id: $id"
                        )
                    }, onFailure = { error ->
                        logger.e(
                            "CustomScreenViewModel switch switch",
                            "Could switch with error: $error"
                        )
                    })
                }.onCompletion {
                    updateState(_uiState.value.copy(isUpdating = false))
                }.launchIn(viewModelScope)
            }
        }
    }

    private fun deleteWidget(widgetId: Int) {
        val updatedWidgets = _uiState.value.widgets.filter { it.id != widgetId }
        logger.d("Deleting widget with id$widgetId", "New widgets: $updatedWidgets")
        updateWidgets(updatedWidgets)
    }

    private fun updateWidget(newState: CustomWidget) {
        logger.d("Updating widgets", "Updating widget with id: ${newState.id}")
        logger.d(
            "Updating widgets", "Available widgets ids: ${_uiState.value.widgets.map { it.id }}"
        )
        val updatedWidgets =
            _uiState.value.widgets.map { if (it.id == newState.id) newState else it }
        logger.d("Updating widgets", "New widgets: $updatedWidgets")
        updateWidgets(updatedWidgets)
    }

    fun handleEvent(event: Event) {
        when (event) {
            is Event.OnSensorStateChanged -> onSwitchUpdated(
                event.sensorId
            )

            is Event.ChosenManySwitchesChange -> updateWidget(
                newState = SensorsList(
                    sensorsIds = event.chosenIds, id = event.widgetId
                )
            )

            Event.LoadData -> loadData()
            Event.ToggleDropDownMenu -> toggleDropDownMenu()
            is Event.DeleteWidget -> deleteWidget(event.widgetId)
            Event.ToggleEditMode -> toggleEditMode()
            is Event.ChosenSingleSwitchChange -> updateWidget(
                CustomWidget.SingleSensor(
                    sensorId = event.chosenId, id = event.widgetId
                )
            )
        }
    }
}
