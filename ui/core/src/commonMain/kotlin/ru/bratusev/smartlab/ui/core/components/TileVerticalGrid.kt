package ru.bratusev.smartlab.ui.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.ui.core.models.TileButtonUi
import ru.bratusev.smartlab.ui.core.theme.AppTheme

@Composable
fun TileVerticalGrid(
    modifier: Modifier = Modifier,
    title: String,
    data: List<TileButtonUi>,
) {
    Box(modifier = modifier.background(MaterialTheme.colorScheme.surface)) {
        Text(
            modifier = Modifier.wrapContentWidth().zIndex(1f).padding(start = 10.dp),
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        LazyVerticalGrid(
            contentPadding = PaddingValues(top = 20.dp),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.Center,
            content = {
                items(data) {
                    TileButton(
                        modifier = Modifier.padding(10.dp), tileButtonUi = it, onClick = {})
                }
            })
    }
}

@Preview(
    group = "TileVerticalGrid", name = "LightBulbs", showBackground = true
)
@Composable
private fun TileVerticalGridPreviewLightBulbs() {
    val mockData = buildList {
        for (i in 1..30) {
            add(
                TileButtonUi.LightBulb(
                    location = "Preview$i",
                    isOn = (0..1).random() == 1,
                    isEnabled = (0..1).random() == 1,
                )
            )
        }
    }
    AppTheme {
        TileVerticalGrid(
            title = "Light bulbs",
            data = mockData
        )
    }
}

@Preview(
    group = "TileVerticalGrid", name = "Thermometers", showBackground = true
)
@Composable
private fun TileVerticalGridPreviewThermometers() {
    val mockData = buildList {
        for (i in 1..30) {
            add(
                TileButtonUi.Thermometer(
                    location = "Preview$i",
                    temperature = (0..50).random().toFloat(),
                    isEnabled = (0..1).random() == 1,
                )
            )
        }
    }
    AppTheme(darkTheme = true) {
        TileVerticalGrid(
            title = "Thermometers",
            data = mockData
        )
    }
}

@Preview(
    group = "TileVerticalGrid", name = "Combined", showBackground = true
)
@Composable
fun ManyGridsPreview() {
    // Это то, как я на данный момент вижу верстку этого меню

    val mockDataThermometers = buildList {
        for (i in 1..30) {
            add(
                TileButtonUi.Thermometer(
                    location = "Preview$i",
                    temperature = (0..50).random().toFloat(),
                    isEnabled = (0..1).random() == 1,
                )
            )
        }
    }
    val mockDataLightBulbs = buildList {
        for (i in 1..30) {
            add(
                TileButtonUi.LightBulb(
                    location = "Preview$i",
                    isOn = (0..1).random() == 1,
                    isEnabled = (0..1).random() == 1,
                )
            )
        }
    }

    AppTheme {
        LazyRow {
            item {
                TileVerticalGrid(
                    modifier = Modifier.width(300.dp),
                    title = "LightBulbs1",
                    data = mockDataLightBulbs,
                )
            }
            item {
                TileVerticalGrid(
                    modifier = Modifier.width(300.dp),
                    title = "Thermometers1",
                    data = mockDataThermometers
                )
            }
            item {
                TileVerticalGrid(
                    modifier = Modifier.width(300.dp),
                    title = "LightBulbs2",
                    data = mockDataLightBulbs
                )
            }
            item {
                TileVerticalGrid(
                    modifier = Modifier.width(300.dp),
                    title = "Thermometers2",
                    data = mockDataThermometers
                )
            }
        }
    }
}

