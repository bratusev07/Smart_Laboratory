package ru.bratusev.smartlab.data.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AreaEntity(
    @SerialName("area_id")
    val areaId: String,
    @SerialName("name")
    val name: String,
    @SerialName("floor_id")
    val floorId: String?,
    @SerialName("labels")
    val labels: List<String>,
    @SerialName("humidity_entity_id")
    val humidityEntityId: String?,
    @SerialName("temperature_entity_id")
    val temperatureEntityId: String?,
    @SerialName("picture")
    val pictureUrl: String?,
    @SerialName("created_at")
    val createdAt: Double,
    @SerialName("modified_at")
    val modifiedAt: Double,
)
