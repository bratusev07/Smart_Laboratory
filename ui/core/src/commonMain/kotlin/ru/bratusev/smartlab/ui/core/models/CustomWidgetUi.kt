package ru.bratusev.smartlab.ui.core.models

import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi

sealed class CustomWidgetUi(
    val id: Int,
) {
    abstract fun toDomain()
    class SensorsList(val sensors: List<SensorCardUi.Widget.Switchs>, index: Int) : CustomWidgetUi(index) {
        override fun toDomain(): CustomWidget {
            TODO("Not yet implemented")
        }
    }
}
