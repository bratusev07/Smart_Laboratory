package ru.bratusev.smartlab.feature_area.models

import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi

data class AreaScreenState(
    val screenName: String = "CustomScreen Screen",
    val areaDevices: List<SensorCardUi.Row> = emptyList()
)

sealed class Event {
    data object ToggleDropDownMenu : Event()
    data class LoadDevices(val areaId: String) : Event()
}