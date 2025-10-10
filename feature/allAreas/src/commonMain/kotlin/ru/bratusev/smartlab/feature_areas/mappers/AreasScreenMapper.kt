package ru.bratusev.smartlab.feature_areas.mappers

import ru.bratusev.smartlab.domain.core.model.socket.Area
import ru.bratusev.smartlab.ui.core.models.AreaCardUi

internal fun Area.toDomain(): AreaCardUi {
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