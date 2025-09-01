package ru.bratusev.smartlab.feature_home.models

import ru.bratusev.smartlab.domain.core.model.socket.ServiceEntity
import ru.bratusev.smartlab.feature_home.mappers.mapToUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardGridPagerUi


data class HomeState(
    val screenName: String = "Home Screen",
    val isUpdating: Boolean = false,
    val serviceEntities: List<ServiceEntity> = emptyList(),
    val socketErrors: List<String> = emptyList(),
) {
    val sensorCardGridPagerUiData: SensorCardGridPagerUi
        get() {
            val result = SensorCardGridPagerUi(
                sensors = serviceEntities.filter { it.domain == "switch" || it.domain == "button" }
                    .map { it.mapToUi() }, verticalGridsAtOneScreen = 1, isLoading = isUpdating
            )
            return result
        }
}

sealed class Event {
    data object OnBackClicked : Event()
    data object OnCustomButtonClicked : Event()
    data class OnButtonTextUpdated(val text: String) : Event()
    data class OnSwitchUpdated(val switchId: String) : Event()
}