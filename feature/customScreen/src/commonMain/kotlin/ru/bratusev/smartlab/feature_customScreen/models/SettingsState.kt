package ru.bratusev.smartlab.feature_customScreen.models

import ru.bratusev.smartlab.ui.core.models.CustomWidgetUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorState

data class CustomScreenState(
    val screenName: String = "CustomScreen Screen",

    val isModalOpen: Boolean = false,
    val widgets: List<CustomWidgetUi> = emptyList(),
)

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
}