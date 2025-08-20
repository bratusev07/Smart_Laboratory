package ru.bratusev.smartlab.ui.core.models

import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorState

sealed class CustomWidgetEvent {
    data class SensorStateChange(val sensorId: String, val newState: SensorState): CustomWidgetEvent()
}