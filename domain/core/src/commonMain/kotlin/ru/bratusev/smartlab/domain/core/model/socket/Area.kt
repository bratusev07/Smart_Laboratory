package ru.bratusev.smartlab.domain.core.model.socket

data class Area(
    val areaId: String,
    val name: String,
    val floorId: String?,
    val labels: List<String>,
    val humidityEntityId: String?,
    val temperatureEntityId: String?,
    val pictureUrl: String?,
    val createdAt: Double,
    val modifiedAt: Double,
)