package ru.bratusev.smartlab.feature_area.models

import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi

data class AreaScreenState(
    val screenName: String = "CustomScreen Screen",
    val areaId: String? = null,
    val areaDevices: List<SensorCardUi.Row> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

sealed class Event {
    data object ToggleDropDownMenu : Event()
    data class FetchData(val areaId: String): Event()
}