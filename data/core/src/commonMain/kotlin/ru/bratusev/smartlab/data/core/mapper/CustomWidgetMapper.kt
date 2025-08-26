package ru.bratusev.smartlab.data.core.mapper

import ru.bratusev.smartlab.data.core.model.CustomWidgetEntity
import ru.bratusev.smartlab.domain.core.model.CustomWidget

fun CustomWidget.toEntity(): CustomWidgetEntity {
    return when (this) {
        is CustomWidget.SensorsList -> CustomWidgetEntity.SensorsList(this.sensorsIds, this.id)
    }
}