package ru.bratusev.smartlab.ui.core.models

import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi

sealed class CustomWidgetUi(
    val id: Int,
) {
    class SensorsList(val sensors: List<SensorCardUi.Widget.Switchs>, index: Int) :
        CustomWidgetUi(index)
}
