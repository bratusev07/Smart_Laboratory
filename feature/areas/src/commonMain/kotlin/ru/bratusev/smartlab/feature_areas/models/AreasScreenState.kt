package ru.bratusev.smartlab.feature_areas.models

import ru.bratusev.smartlab.ui.core.models.AreaCardUi

data class AreasScreenState(
    val screenName: String = "CustomScreen Screen",
    val areas: List<AreaCardUi> = emptyList()
)

sealed class Event {
    data object ToggleDropDownMenu : Event()
}