package ru.bratusev.smartlab.feature_customScreen.models

data class CustomScreenState(
    val screenName: String = "CustomScreen Screen"
)

sealed class Event {
    data object OnBackClicked: Event()
    data object OnCustomButtonClicked : Event()
    data class OnButtonTextUpdated(val text: String) : Event()
}