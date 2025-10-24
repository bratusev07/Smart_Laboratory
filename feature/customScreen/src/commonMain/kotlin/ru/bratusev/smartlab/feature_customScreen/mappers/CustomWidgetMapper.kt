package ru.bratusev.smartlab.feature_customScreen.mappers

import ru.bratusev.smartlab.domain.core.model.CustomWidget
import ru.bratusev.smartlab.domain.core.model.socket.ServiceEntity
import ru.bratusev.smartlab.ui.core.models.CustomWidgetUi
import ru.bratusev.smartlab.ui.core.models.CustomWidgetUi.ManySensorsList
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardRes
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardTints
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi.Modal
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi.Widget.Switch
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorDomain
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorState

fun CustomWidget.toUi(sensors: List<ServiceEntity>, id: Int): CustomWidgetUi {
    return when (this) {
        is CustomWidget.SensorsList ->
            ManySensorsList(
                sensorsToShow = sensors.filter { it.id in this.sensorsIds }.map { switch ->
                    Switch(
                        title = switch.attributes?.friendlyName.orEmpty(),
                        id = switch.id!!,
                        state = SensorState.fromString(switch.state),
                        domain = SensorDomain.fromString(switch.domain),
                        drawableResource = SensorCardRes.lightBulb,
                        tints = SensorCardTints.Common.LightBulb
                    )
                },
                id = id,
                sensorsToChooseFrom = sensors.map { switch ->
                    Modal(
                        title = switch.attributes?.friendlyName.orEmpty(),
                        id = switch.id!!,
                        state = SensorState.fromString(switch.state),
                        domain = SensorDomain.fromString(switch.domain),
                        drawableResource = SensorCardRes.lightBulb,
                        tints = SensorCardTints.Common.LightBulb
                    )
                },
                title = title,
                isEditMode = false,
                openModal = false,
            )

        is CustomWidget.SingleSensor -> {
            CustomWidgetUi.SingleSensor(
                sensor =
                    sensors.find { it.id == this.sensorId }?.let {
                        Switch(
                            title = it.attributes?.friendlyName.orEmpty(),
                            id = sensorId,
                            state = SensorState.fromString(it.state),
                            domain = SensorDomain.fromString(it.domain),
                            drawableResource = SensorCardRes.lightBulb,
                            tints = SensorCardTints.Common.LightBulb
                        )
                    } ?: Switch(
                        title = "Отсутствует",
                        id = "Отсутствует",
                        state = SensorState.Unavailable,
                        domain = SensorDomain.SWITCH,
                        drawableResource = SensorCardRes.lightBulb,
                        tints = SensorCardTints.Common.LightBulb
                    ),
                id = id,
                sensorsToChooseFrom = sensors.map { switch ->
                    Modal(
                        title = switch.attributes?.friendlyName.orEmpty(),
                        id = switch.id!!,
                        state = SensorState.fromString(switch.state),
                        domain = SensorDomain.fromString(switch.domain),
                        drawableResource = SensorCardRes.lightBulb,
                        tints = SensorCardTints.Common.LightBulb
                    )
                },
                title = title,
                isEditMode = false,
                openModal = false
            )
        }
    }
}