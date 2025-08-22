package ru.bratusev.smartlab.feature_home.mappers

import ru.bratusev.smartlab.domain.core.model.socket.ServiceEntity
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardGridPagerUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardRes
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardTints
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardVerticalGridUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorState
import ru.bratusev.smartlab.ui.core.theme.SensorCardCommonColors

fun List<ServiceEntity>.mapToServiceListUi() = SensorCardVerticalGridUi(
    sensors = this.map { it.mapToUi() },
    columnsAmount = 2
)

fun List<ServiceEntity>.mapToServicePagerUi() = SensorCardGridPagerUi(
    sensors = this.map { it.mapToUi() },
    verticalGridsAtOneScreen = 1
)

private fun ServiceEntity.mapToUi(): SensorCardUi.Tile = when (domain?.lowercase()) {
    "switch" -> this.mapSwitchToUi()
    "button" -> this.mapButtonToUi()
    else -> this.mapDefaultToUi()
}

private fun ServiceEntity.mapDefaultToUi() = SensorCardUi.Tile.Small(
    id = id.orEmpty(),
    state = SensorState.fromString(state),
    domain = domain.orEmpty(),
    drawableResource = SensorCardRes.lightBulb,
    tints = SensorCardTints(
        SensorCardCommonColors.LightBulb.On,
        SensorCardCommonColors.LightBulb.Off,
        SensorCardCommonColors.LightBulb.Unavailable
    ),
)

private fun ServiceEntity.mapSwitchToUi() = SensorCardUi.Tile.Medium(
    title = id.orEmpty(),
    id = id.orEmpty(),
    state = SensorState.fromString(state),
    domain = domain.orEmpty(),
    drawableResource = SensorCardRes.lightBulb,
    tints = SensorCardTints(
        SensorCardCommonColors.LightBulb.On,
        SensorCardCommonColors.LightBulb.Off,
        SensorCardCommonColors.LightBulb.Unavailable
    ),
)

private fun ServiceEntity.mapButtonToUi() = SensorCardUi.Tile.Medium(
    title = id.orEmpty(),
    id = id.orEmpty(),
    state = SensorState.fromString(state),
    domain = domain.orEmpty(),
    drawableResource = SensorCardRes.lightBulb,
    tints = SensorCardTints(
        SensorCardCommonColors.LightBulb.Off,
        SensorCardCommonColors.LightBulb.Off,
        SensorCardCommonColors.LightBulb.Unavailable
    ),
)

