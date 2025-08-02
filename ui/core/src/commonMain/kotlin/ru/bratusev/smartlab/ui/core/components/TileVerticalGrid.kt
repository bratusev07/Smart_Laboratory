package ru.bratusev.smartlab.ui.core.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import ru.bratusev.smartlab.ui.core.models.TileButtonUi
import ru.bratusev.smartlab.ui.core.theme.AppTheme

@Composable
fun TileVerticalGrid(
    modifier: Modifier = Modifier,
    title: String,
    data: List<TileButtonUi>,
    columnsAmount: Int = 2,
) {
    var isReversed by rememberSaveable { mutableStateOf(false) }

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(columnsAmount),
        horizontalArrangement = Arrangement.Center,
        content = {
            stickyHeader {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Button(
                        onClick = { isReversed = !isReversed }) {
                        Text("TODO")
                    }
                }
            }
            items(data.reversedIf(isReversed)) {
                TileButton(
                    modifier = Modifier.padding(10.dp), tileButtonUi = it, onClick = {})
            }
        })
}

//
private fun <E> List<E>.reversedIf(flag: Boolean): List<E> {
    return if (flag) this.reversed()
    else this
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
            title = "Light bulbs", data = mockData
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
            title = "Thermometers", data = mockData
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
        Row {
            Text(modifier = Modifier.width(100.dp), text = "Боковое менб")
            LazyRow(
                modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    TileVerticalGrid(
                        modifier = Modifier.fillParentMaxWidth(),
                        title = "LightBulbs1",
                        data = mockDataLightBulbs,
                    )
                }
                item {
                    TileVerticalGrid(
                        modifier = Modifier.fillParentMaxWidth(),
                        title = "Thermometers1",
                        data = mockDataThermometers
                    )
                }
                item {
                    TileVerticalGrid(
                        modifier = Modifier.fillParentMaxWidth(),
                        title = "LightBulbs2",
                        data = mockDataLightBulbs
                    )
                }
                item {
                    TileVerticalGrid(
                        modifier = Modifier.fillParentMaxWidth(),
                        title = "Thermometers2",
                        data = mockDataThermometers
                    )
                }
            }
        }
    }
}

