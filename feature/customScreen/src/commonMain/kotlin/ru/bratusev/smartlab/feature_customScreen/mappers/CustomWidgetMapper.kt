package ru.bratusev.smartlab.feature_customScreen.mappers

import ru.bratusev.smartlab.domain.core.model.CustomWidget
import ru.bratusev.smartlab.domain.core.model.socket.ServiceEntity
import ru.bratusev.smartlab.ui.core.models.CustomWidgetUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardRes
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardTints
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorDomain
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorState

fun CustomWidget.toUi(sensors: List<ServiceEntity>, id: Int): CustomWidgetUi {
    return when (this) {
        is CustomWidget.SensorsList ->
            CustomWidgetUi.SensorsList(
                sensorsToShow = sensors.filter { it.id in this.sensorsIds }.map { switch ->
                    SensorCardUi.Widget.Switches(
                        title = "title ${switch.id}",
                        id = switch.id!!,
                        state = SensorState.fromString(switch.state),
                        domain = SensorDomain.fromString(switch.domain),
                        drawableResource = SensorCardRes.lightBulb,
                        tints = SensorCardTints.Common.LightBulb
                    )
                },
                id = id,
                sensorsToChooseFrom = sensors.map { switch ->
                    SensorCardUi.Modal(
                        title = "title ${switch.id}",
                        id = switch.id!!,
                        state = SensorState.fromString(switch.state),
                        domain = SensorDomain.fromString(switch.domain),
                        drawableResource = SensorCardRes.lightBulb,
                        tints = SensorCardTints.Common.LightBulb
                    )
                },
                isEditMode = false
            )
    }
}