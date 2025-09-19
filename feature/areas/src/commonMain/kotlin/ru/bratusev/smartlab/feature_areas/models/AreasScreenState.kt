package ru.bratusev.smartlab.feature_areas.models

data class AreasScreenState(
    val screenName: String = "CustomScreen Screen",

) {
}

sealed class Event {
    data object ToggleDropDownMenu : Event()
}