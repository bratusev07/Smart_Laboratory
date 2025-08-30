package ru.bratusev.smartlab.ui.core.models

import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi

sealed class CustomWidgetUi {
    abstract val id: Int
    abstract val isEditMode: Boolean
    abstract fun copy(
        id: Int = this.id,
        isEditMode: Boolean = this.isEditMode,
    ): CustomWidgetUi

    data class SensorsList(
        val sensorsToShow: List<SensorCardUi.Widget.Switches>,
        val sensorsToChooseFrom: List<SensorCardUi.Modal>,
        override val id: Int,
        override val isEditMode: Boolean = false,
    ) :
        CustomWidgetUi() {
        override fun copy(
            id: Int,
            isEditMode: Boolean,
        ): CustomWidgetUi = copy(
            id = id,
            isEditMode = isEditMode,
            sensorsToShow = sensorsToShow,
            sensorsToChooseFrom = sensorsToChooseFrom
        )
    }
}
