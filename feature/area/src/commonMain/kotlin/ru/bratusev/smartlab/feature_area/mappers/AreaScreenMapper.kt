package ru.bratusev.smartlab.feature_area.mappers

import ru.bratusev.smartlab.domain.core.model.socket.Area
import ru.bratusev.smartlab.domain.core.model.socket.ServiceEntity
import ru.bratusev.smartlab.ui.core.models.AreaCardUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardRes
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardTints
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorDomain
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorState

internal fun Area.mapToUi(): AreaCardUi {
    // TODO: Сделать получение значений температуры и влажности
    return AreaCardUi(
        areaId = this.areaId,
        name = this.name,
        floorId = this.floorId,
        labels = this.labels,
        humidity = null,
        temperature = null,
        pictureUrl = this.pictureUrl,
        createdAt = this.createdAt,
        modifiedAt = this.modifiedAt
    )
}

internal fun ServiceEntity.mapToUi() = SensorCardUi.Row(
    title = attributes?.friendlyName ?: id ?: "empty name",
    id = id ?: "empty id",
    state = SensorState.fromString(state),
    domain = SensorDomain.fromString(domain),
    mdiIcon = attributes?.icon.toString(),
    tints = SensorCardTints.Common.LightBulb
)