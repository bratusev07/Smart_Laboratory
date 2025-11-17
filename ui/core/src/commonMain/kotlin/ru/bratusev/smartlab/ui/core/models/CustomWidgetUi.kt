package ru.bratusev.smartlab.ui.core.models

import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi

sealed class CustomWidgetUi {
    abstract val id: Int
    abstract val title: String?
    abstract val isEditMode: Boolean
    abstract val isAddMode: Boolean
    abstract val showAddButton: Boolean
    abstract fun copy(
        id: Int = this.id,
        isEditMode: Boolean = this.isEditMode,
        isAddMode: Boolean = this.isAddMode
    ): CustomWidgetUi

    data class ManySensorsList(
        val sensorsToShow: List<SensorCardUi.Widget.Switch>,
        val sensorsToChooseFrom: List<SensorCardUi.Modal>,
        val openModal: Boolean,
        override val id: Int,
        override val title: String? = null,
        override val isEditMode: Boolean = false,
        override val isAddMode: Boolean = false,
        override val showAddButton: Boolean = true
    ) :
        CustomWidgetUi() {
        override fun copy(
            id: Int,
            isEditMode: Boolean,
            isAddMode: Boolean
        ): CustomWidgetUi = copy(
            id = id,
            isEditMode = isEditMode,
            isAddMode = isAddMode,
            showAddButton = showAddButton,
            sensorsToShow = sensorsToShow,
            sensorsToChooseFrom = sensorsToChooseFrom,
        )
    }

    data class SingleSensor(
        val sensor: SensorCardUi.Widget.Switch,
        val sensorsToChooseFrom: List<SensorCardUi.Modal>,
        val openModal: Boolean,
        override val id: Int,
        override val title: String? = null,
        override val isEditMode: Boolean = false,
        override val isAddMode: Boolean = false,
        override val showAddButton: Boolean = false
    ) :
        CustomWidgetUi() {
        override fun copy(
            id: Int,
            isEditMode: Boolean,
            isAddMode: Boolean,
        ): CustomWidgetUi = copy(
            id = id,
            isEditMode = isEditMode,
            isAddMode = isAddMode,
            sensor = sensor,
            sensorsToChooseFrom = sensorsToChooseFrom,
            showAddButton = showAddButton
        )

        companion object {
            const val NO_SENSOR_ID = "Не назначено"
            const val NO_SENSOR_TITLE = "Не назначено"
        }
    }
}
