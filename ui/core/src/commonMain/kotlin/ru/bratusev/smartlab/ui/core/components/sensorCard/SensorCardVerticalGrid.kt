package ru.bratusev.smartlab.ui.core.components.sensorCard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardRes
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardState
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardTints
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardVerticalGridUi
import ru.bratusev.smartlab.ui.core.theme.AppTheme

@Composable
fun SensorCardVerticalGrid(
    modifier: Modifier = Modifier,
    uiData: SensorCardVerticalGridUi,
) {
    var isReversed by rememberSaveable { mutableStateOf(false) }

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(uiData.columnsAmount),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        content = {
            stickyHeader {
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { isReversed = !isReversed }) {
                        Text("TODO")
                    }
                }
            }
            items(uiData.tiles.reversedIf(isReversed), key = { it.hashCode() }) {
                SensorCard(
                    modifier = Modifier, sensorCardUi = it, onClick = {})
            }
        })
}

private fun <E> List<E>.reversedIf(flag: Boolean): List<E> {
    return if (flag) this.reversed()
    else this
}

@Preview(
    group = "SensorCardVerticalGrid", name = "LightBulbs", showBackground = true
)
@Composable
private fun SensorCardVerticalGridLightBulbs() {
    val mockData = SensorCardVerticalGridUi(
        title = "Light bulbs",
        buildList<SensorCardUi.Medium> {
            for (i in 1..30) {
                add(
                    SensorCardUi.Medium(
                        title = "Preview$i",
                        id = "Id$i",
                        state = SensorCardState.entries[(0..2).random()],
                        domain = "PreviewDomain$i",
                        drawableResource = SensorCardRes.lightBulb,
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
        )
    }
}

@Preview(
    group = "SensorCardVerticalGrid", name = "Thermometers", showBackground = true
)
@Composable
private fun SensorCardVerticalGridThermometers() {
    val mockData = SensorCardVerticalGridUi(
        title = "Light bulbs",
        buildList {
            for (i in 1..30) {
                add(
                    SensorCardUi.Medium(
                        title = "Preview$i",
                        id = "Id$i",
                        state = SensorCardState.entries[(0..1).random()],
                        domain = "PreviewDomain$i",
                        drawableResource = SensorCardRes.thermometer,
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
        )
    }
}
