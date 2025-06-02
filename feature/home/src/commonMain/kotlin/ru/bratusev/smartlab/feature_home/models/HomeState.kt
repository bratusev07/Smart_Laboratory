package ru.bratusev.smartlab.feature_home.models

data class HomeState(
    val screenName: String = "Home Screen"
)

sealed class Event {
    data object OnBackClicked: Event()
    data object OnCustomButtonClicked : Event()
    data class OnButtonTextUpdated(val text: String) : Event()
}