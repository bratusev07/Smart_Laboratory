package ru.bratusev.smartlab.data.core.mapper

import ru.bratusev.smartlab.data.core.model.AreaEntity
import ru.bratusev.smartlab.domain.core.model.socket.Area
import ru.bratusev.smartlab.domain.core.repository.ServerSelectionRepository

class AreaEntityMapper(private val serverSelectionRepository: ServerSelectionRepository) {
    internal fun mapToDomain(areaEntity: AreaEntity): Area {
        with(areaEntity) {
            val actualPictureUrl = pictureUrl?.let {
                if (!it.contains("http")) {
                    serverSelectionRepository.getCurrentBaseUrl() + pictureUrl
                } else {
                    pictureUrl
                }
            }
            return Area(
                areaId = areaId,
                name = name,
                floorId = floorId,
                labels = labels,
                humidityEntityId = humidityEntityId,
                temperatureEntityId = temperatureEntityId,
                pictureUrl = actualPictureUrl,
                createdAt = createdAt,
                modifiedAt = modifiedAt
            )
        }
    }
}