package ru.bratusev.smartlab.ui.core.models.sensorCard

data class SensorCardGridPagerUi(
    val sensors: List<SensorCardUi>,
    val verticalGridsAtOneScreen: Int = 1,
)