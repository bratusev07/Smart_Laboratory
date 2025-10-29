package ru.bratusev.smartlab.ui.core.models

data class AreaCardUi(
    val areaId: String,
    val name: String,
    val floorId: String?,
    val labels: List<String>,
    val humidity: Float?,
    val temperature: Float?,
    val pictureUrl: String?,
    val createdAt: Double,
    val modifiedAt: Double,
    val isClickable: Boolean = true
)
