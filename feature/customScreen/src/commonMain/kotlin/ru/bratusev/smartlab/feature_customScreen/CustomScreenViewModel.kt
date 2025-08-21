package ru.bratusev.smartlab.feature_customScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.bratusev.smartlab.domain.core.model.socket.ServiceEntity
import ru.bratusev.smartlab.domain.core.usecase.GetCustomWidgetsUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetLoggerUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetServiceEntitiesUseCase
import ru.bratusev.smartlab.domain.core.usecase.SetCustomWidgetsUseCase
import ru.bratusev.smartlab.feature_customScreen.mappers.toDomain
import ru.bratusev.smartlab.feature_customScreen.mappers.toUi
import ru.bratusev.smartlab.feature_customScreen.models.CustomScreenState
import ru.bratusev.smartlab.feature_customScreen.models.Event
import ru.bratusev.smartlab.ui.core.models.CustomWidgetUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardRes
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardTints
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorState

class CustomScreenViewModel(
    getServiceEntitiesUseCase: GetServiceEntitiesUseCase,
    private val getWidgets: GetCustomWidgetsUseCase,
    private val saveWidgets: SetCustomWidgetsUseCase,
    private val logger: GetLoggerUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomScreenState())
    val uiState: StateFlow<CustomScreenState> = _uiState

    init {
        getServiceEntitiesUseCase.invoke().onEach {
            onServiceEntitiesUpdated(it)
        }.launchIn(viewModelScope)

        var index = 0
        getWidgets().onEach { result ->
            result.onSuccess {
                val resultWidgets =
                    it.map { widget -> widget.toUi(_uiState.value.switchesEntities, index++) }
                        .ifEmpty {
                            logger.d(
                                "CustomScreen/init",
                                "Custom widgets store is empty. Creating new."
                            )
                            getWidgetsPreview()
                        }
                updateState(_uiState.value.copy(widgets = resultWidgets))
            }
            result.onFailure { error ->
                getWidgetsPreview()
                logger.e("CustomScreen/init", "Error during getting widgets. Error: $error")
            }
        }.launchIn(viewModelScope)

        updateState(_uiState.value.copy(widgets = getWidgetsPreview()))
    }

    private fun onServiceEntitiesUpdated(entities: List<ServiceEntity>) {
        val switches: List<ServiceEntity> = entities.filter { it.domain == "switch" }
        updateState(uiState.value.copy(switchesEntities = switches))
    }


    private fun getWidgetsPreview(): List<CustomWidgetUi> {
        val data = buildList {
            for (i in 1..30) {
                add(
                    SensorCardUi.Widget.Switchs(
                        title = "Preview$i",
                        id = "Id$i",
                        state = SensorState.entries[(0..2).random()],
                        domain = "PreviewDomain$i",
                        drawableResource = SensorCardRes.lightBulb,
                        tints = SensorCardTints.Common.LightBulb
                    )
                )
            }
        }

        val widgets = emptyList<CustomWidgetUi>().toMutableList()
        widgets.add(
            CustomWidgetUi.SensorsList(
                sensors = data, index = 1
            )
        )
        widgets.add(
            CustomWidgetUi.SensorsList(
                sensors = data.reversed(), index = 2
            )
        )
        widgets.add(
            CustomWidgetUi.SensorsList(
                sensors = data, index = 3
            )
        )

        saveWidgets.invoke(widgets.map { it.toDomain() }).launchIn(viewModelScope)
        return widgets
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

    private fun changeSensorState(widgetId: Int, sensorId: String, newState: SensorState) {
        // TODO: сделать реальную обработку на сервере или типа того
        val widget = _uiState.value.widgets.find { it.id == widgetId } ?: return
        when (widget) {
            is CustomWidgetUi.SensorsList -> {
                val updatedSensorList = widget.sensors.map { sensor ->
                    if (sensor.id == sensorId) sensor.copy(state = newState) else sensor
                }
                val updatedWidget =
                    CustomWidgetUi.SensorsList(sensors = updatedSensorList, index = widgetId)
                updateWidget(widgetId, updatedWidget)
            }
        }
    }

    private fun updateWidget(widgetId: Int, newState: CustomWidgetUi) {
        val updatedWidgets = _uiState.value.widgets.map { if (it.id == widgetId) newState else it }
        updateState(_uiState.value.copy(widgets = updatedWidgets))
    }

    internal fun handleEvent(event: Event) {
        when (event) {
            Event.OnBackClicked -> Unit
            Event.OnCustomButtonClicked -> onCustomButtonClicked()
            is Event.OnButtonTextUpdated -> onButtonTextUpdated(event.text)
            Event.OnMenuButtonClicked -> toggleModalMenu()
            Event.OnModalCloseClicked -> toggleModalMenu()
            is Event.OnSensorStateChanged -> changeSensorState(
                widgetId = event.widgetId, sensorId = event.sensorId, newState = event.newState
            )
        }
    }
}