package ru.bratusev.smartlab.feature_customScreen.mappers

import ru.bratusev.smartlab.domain.core.model.CustomWidget
import ru.bratusev.smartlab.ui.core.models.CustomWidgetUi

fun CustomWidgetUi.toDomain(): CustomWidget {
    return when (this) {
        is CustomWidgetUi.SensorsList -> CustomWidget.SensorsList(
            sensorsIds = sensorsToShow.map { it.id },
            id = id
        )
    }
}