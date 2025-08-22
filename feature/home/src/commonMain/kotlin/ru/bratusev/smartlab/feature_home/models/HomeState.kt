package ru.bratusev.smartlab.feature_home.models

import ru.bratusev.smartlab.domain.core.model.socket.ServiceEntity


data class HomeState(
    val screenName: String = "Home Screen",
    val serviceEntities: List<ServiceEntity> = emptyList(),
    val socketErrors: List<String> = emptyList()
)

sealed class Event {
    data object OnBackClicked: Event()
    data object OnCustomButtonClicked : Event()
    data class OnButtonTextUpdated(val text: String) : Event()
    data class OnSwitchUpdated(val switchId: String) : Event()
}