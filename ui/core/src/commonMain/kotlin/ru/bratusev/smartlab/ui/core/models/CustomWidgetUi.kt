package ru.bratusev.smartlab.ui.core.models

import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi

sealed class CustomWidgetUi(
    val id: Int,
) {
    class SensorsList(
        val sensorsToShow: List<SensorCardUi.Widget.Switches>,
        val sensorsToChooseFrom: List<SensorCardUi.Modal>,
        id: Int,
    ) :
        CustomWidgetUi(id)
}
