package ru.bratusev.smartlab.ui.core.components.tileButton

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
import ru.bratusev.smartlab.ui.core.models.tileButton.TileButtonUi
import ru.bratusev.smartlab.ui.core.models.tileButton.VerticalTileGridUi
import ru.bratusev.smartlab.ui.core.theme.AppTheme

@Composable
fun VerticalTileGrid(
    modifier: Modifier = Modifier,
    uiData: VerticalTileGridUi,
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
                TileButton(
                    modifier = Modifier, tileButtonUi = it, onClick = {})
            }
        })
}

private fun <E> List<E>.reversedIf(flag: Boolean): List<E> {
    return if (flag) this.reversed()
    else this
}

@Preview(
    group = "TileVerticalGrid", name = "LightBulbs", showBackground = true
)
@Composable
private fun TileVerticalGridPreviewLightBulbs() {
    val mockData = VerticalTileGridUi(title = "Light bulbs", buildList {
        for (i in 1..30) {
            add(
                TileButtonUi.LightBulb(
                    location = "Preview$i",
                    isOn = (0..1).random() == 1,
                    isEnabled = (0..1).random() == 1,
                )
            )
        }
    })
    AppTheme {
        VerticalTileGrid(
            uiData = mockData
        )
    }
}

@Preview(
    group = "TileVerticalGrid", name = "Thermometers"
)
@Composable
private fun TileVerticalGridPreviewThermometers() {
    val mockData = VerticalTileGridUi(title = "Thermometers", buildList {
        for (i in 1..30) {
            add(
                TileButtonUi.Thermometer(
                    location = "Preview$i",
                    temperature = (0..50).random().toFloat(),
                    isEnabled = (0..1).random() == 1,
                )
            )
        }
    })
    AppTheme(darkTheme = true) {
        VerticalTileGrid(
            uiData = mockData
        )
    }
}
