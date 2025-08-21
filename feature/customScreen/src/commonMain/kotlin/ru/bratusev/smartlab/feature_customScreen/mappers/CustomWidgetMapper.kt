package ru.bratusev.smartlab.feature_customScreen.mappers

import ru.bratusev.smartlab.domain.core.model.CustomWidget
import ru.bratusev.smartlab.domain.core.model.socket.ServiceEntity
import ru.bratusev.smartlab.ui.core.models.CustomWidgetUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardRes
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardTints
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorState

fun CustomWidget.toUi(sensors: List<ServiceEntity>, index: Int): CustomWidgetUi {
    return when (this) {
        is CustomWidget.SensorsList ->
            CustomWidgetUi.SensorsList(
                sensors = sensors.filter { it.id in this.sensorsIds }.map { switch ->
                    SensorCardUi.Widget.Switchs(
                        title = "title ${switch.id}",
                        id = switch.id!!,
                        state = SensorState.fromString(switch.state.toString()),
                        domain = switch.domain ?: "domain empty",
                        drawableResource = SensorCardRes.lightBulb,
                        tints = SensorCardTints.Common.LightBulb
                    )
                },
                index = index
            )
    }
}