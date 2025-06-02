package ru.bratusev.smartlab.feature_settings.models

data class SettingsState(
    val screenName: String = "Settings Screen"
)

sealed class Event {
    data object OnBackClicked: Event()
    data object OnCustomButtonClicked : Event()
    data class OnButtonTextUpdated(val text: String) : Event()
}