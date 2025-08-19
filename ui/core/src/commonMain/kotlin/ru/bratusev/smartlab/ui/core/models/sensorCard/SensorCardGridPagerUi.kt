package ru.bratusev.smartlab.ui.core.models.sensorCard

data class SensorCardGridPagerUi(
    val sensors: List<SensorCardUi.Tile>,
    val verticalGridsAtOneScreen: Int = 1,
)