package ru.bratusev.smartlab.feature_customScreen.models

import ru.bratusev.smartlab.domain.core.model.CustomWidget
import ru.bratusev.smartlab.domain.core.model.socket.ServiceEntity
import ru.bratusev.smartlab.feature_customScreen.mappers.toUi
import ru.bratusev.smartlab.ui.core.models.CustomWidgetUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorState

data class CustomScreenState(
    val screenName: String = "CustomScreen Screen",

    val isModalOpen: Boolean = false,
    val widgets: List<CustomWidget> = emptyList(),
    val switchesEntities: List<ServiceEntity> = emptyList(),
    val socketErrors: List<String> = emptyList(),
) {
    val widgetsUi: List<CustomWidgetUi>
        get() = widgets.map { widget ->
            widget.toUi(
                switchesEntities,
                widget.id
            )
        }
}

sealed class Event {
    data object OnBackClicked : Event()
    data object OnCustomButtonClicked : Event()
    data class OnButtonTextUpdated(val text: String) : Event()

    data object OnMenuButtonClicked : Event()
    object OnModalCloseClicked : Event()

    data class OnSensorStateChanged(
        val widgetId: Int,
        val sensorId: String,
        val newState: SensorState,
    ) : Event()

    data class OnSwitchesWidgetChanged(
        val widgetId: Int,
        val chosenIds: List<String>,
    ) : Event()

    data object LoadData : Event()
}