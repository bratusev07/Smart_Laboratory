package ru.bratusev.smartlab.ui.core.models

import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi

sealed class CustomWidgetUi {
    abstract val id: Int
    abstract val showDeleteButton: Boolean
    abstract fun copy(
        id: Int = this.id,
        showDeleteButton: Boolean = this.showDeleteButton,
    ): CustomWidgetUi

    data class SensorsList(
        val sensorsToShow: List<SensorCardUi.Widget.Switches>,
        val sensorsToChooseFrom: List<SensorCardUi.Modal>,
        override val id: Int,
        override val showDeleteButton: Boolean = false,
    ) :
        CustomWidgetUi() {
        override fun copy(
            id: Int,
            showDeleteButton: Boolean,
        ): CustomWidgetUi = copy(
            id = id,
            showDeleteButton = showDeleteButton,
            sensorsToShow = sensorsToShow,
            sensorsToChooseFrom = sensorsToChooseFrom
        )
    }
}
