package ru.bratusev.smartlab.feature_customScreen.models

import ru.bratusev.smartlab.domain.core.model.CustomWidget
import ru.bratusev.smartlab.domain.core.model.socket.ServiceEntity
import ru.bratusev.smartlab.feature_customScreen.mappers.toUi
import ru.bratusev.smartlab.ui.core.models.CustomWidgetUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorState

data class CustomScreenState(
    val screenName: String = "CustomScreen Screen",
    val isUpdating: Boolean = false,
    val isSaving: Boolean = false,
    val isDropDownMenuExpanded: Boolean = false,
    val isEditMode: Boolean = false,
    val widgets: List<CustomWidget> = emptyList(),
    val switchesEntities: List<ServiceEntity> = emptyList(),
    val socketErrors: List<String> = emptyList(),
) {
    val widgetsUi: List<CustomWidgetUi>
        get() = widgets.map { widget ->
            widget.toUi(
                switchesEntities, widget.id
            )
        }
}

sealed class Event {
    data object ToggleDropDownMenu : Event()
    data object ToggleEditMode : Event()
    data class DeleteWidget(val widgetId: Int) : Event()
    data class OnSensorStateChanged(
        val widgetId: Int,
        val sensorId: String,
        val newState: SensorState,
    ) : Event()

    data class ChosenManySwitchesChange(
        val widgetId: Int,
        val chosenIds: List<String>,
        val title: String
    ) : Event()

    data class ChosenSingleSwitchChange(
        val widgetId: Int,
        val chosenId: String,
        val title: String
    ) : Event()

    data class EditTitle(
        val widgetId: Int,
        val title: String
    ): Event()

    data object LoadData : Event()
}