package ru.bratusev.smartlab.ui.core.components.sensorCard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardRes
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardTints
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardVerticalGridUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorDomain
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorState
import ru.bratusev.smartlab.ui.core.theme.AppTheme

@Composable
fun SensorCardVerticalGrid(
    modifier: Modifier = Modifier,
    uiData: SensorCardVerticalGridUi,
    onSensorCardClicked: (String) -> Unit,
) {

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(uiData.columnsAmount),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        content = {
            stickyHeader {
                SensorCardToolBar(
                    onEditClick = {},
                    onAddClick = {}
                )
            }
            items(uiData.sensors, key = { it.hashCode() }) {
                SensorCardTile(
                    modifier = modifier.fillMaxWidth(),
                    sensorCardUi = it,
                    onClick = { onSensorCardClicked(it.id) })
            }
        })
}

@Preview(
    group = "SensorCardVerticalGrid", name = "LightBulbs", showBackground = true
)
@Composable
private fun SensorCardVerticalGridLightBulbs() {
    val mockData = SensorCardVerticalGridUi(
        buildList {
            for (i in 1..30) {
                add(
                    SensorCardUi.Tile.Medium(
                        title = "Preview$i",
                        id = "Id$i",
                        state = SensorState.On,
                        domain = SensorDomain.SWITCH,
                        mdiIcon = "no icon",
                        tints = SensorCardTints.Common.LightBulb
                    )
                )
            }
        },
        columnsAmount = 2,
    )
    AppTheme {
        SensorCardVerticalGrid(
            uiData = mockData
        ) {}
    }
}

@Preview(
    group = "SensorCardVerticalGrid", name = "Thermometers", showBackground = true
)
@Composable
private fun SensorCardVerticalGridThermometers() {
    val mockData = SensorCardVerticalGridUi(
        buildList {
            for (i in 1..30) {
                add(
                    SensorCardUi.Tile.Medium(
                        title = "Preview$i",
                        id = "Id$i",
                        state = SensorState.On,
                        domain = SensorDomain.SWITCH,
                        mdiIcon = "no icon",
                        tints = SensorCardTints.Common.Thermometer
                    )
                )
            }
        },
        columnsAmount = 2,
    )
    AppTheme(darkTheme = true) {
        SensorCardVerticalGrid(
            uiData = mockData
        ) {}
    }
}
