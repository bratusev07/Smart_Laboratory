package ru.bratusev.smartlab.feature_home.mappers

import ru.bratusev.smartlab.domain.core.model.socket.ServiceEntity
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardGridPagerUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardRes
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardState
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardTints
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardVerticalGridUi
import ru.bratusev.smartlab.ui.core.theme.SensorCardCommonColors

fun List<ServiceEntity>.mapToServiceListUi() = SensorCardVerticalGridUi(
    sensors = this.map { it.mapToUi() },
    columnsAmount = 2
)

fun List<ServiceEntity>.mapToServicePagerUi() = SensorCardGridPagerUi(
    sensors = this.map { it.mapToUi() },
    verticalGridsAtOneScreen = 1
)

private fun ServiceEntity.mapToUi(): SensorCardUi = when (domain?.lowercase()) {
    "switch" -> this.mapSwitchToUi()
    "button" -> this.mapButtonToUi()
    else -> this.mapDefaultToUi()
}

private fun ServiceEntity.mapDefaultToUi() = SensorCardUi.Small(
    id = id.orEmpty(),
    state = state.mapToSensorState(),
    domain = domain.orEmpty(),
    drawableResource = SensorCardRes.lightBulb,
    tints = SensorCardTints(
        SensorCardCommonColors.LightBulb.On,
        SensorCardCommonColors.LightBulb.Off,
        SensorCardCommonColors.LightBulb.Unavailable
    ),
)

private fun ServiceEntity.mapSwitchToUi() = SensorCardUi.Medium(
    title = id.orEmpty(),
    id = id.orEmpty(),
    state = state.mapToSensorState(),
    domain = domain.orEmpty(),
    drawableResource = SensorCardRes.lightBulb,
    tints = SensorCardTints(
        SensorCardCommonColors.LightBulb.On,
        SensorCardCommonColors.LightBulb.Off,
        SensorCardCommonColors.LightBulb.Unavailable
    ),
)

private fun ServiceEntity.mapButtonToUi() = SensorCardUi.Medium(
    title = id.orEmpty(),
    id = id.orEmpty(),
    state = state.mapToSensorState(),
    domain = domain.orEmpty(),
    drawableResource = SensorCardRes.lightBulb,
    tints = SensorCardTints(
        SensorCardCommonColors.LightBulb.Off,
        SensorCardCommonColors.LightBulb.Off,
        SensorCardCommonColors.LightBulb.Unavailable
    ),
)

private fun String?.mapToSensorState() = when (this?.lowercase()) {
    "on", "\"on\"" -> SensorCardState.On
    "off", "\"off\"" -> SensorCardState.Off
    else -> SensorCardState.Unavailable
}
