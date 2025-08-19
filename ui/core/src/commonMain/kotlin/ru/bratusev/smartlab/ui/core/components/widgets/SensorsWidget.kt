package ru.bratusev.smartlab.ui.core.components.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.ui.core.components.sensorCard.SensorCardRow
import ru.bratusev.smartlab.ui.core.models.CustomWidgetUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardRes
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardState
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardTints
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi
import ru.bratusev.smartlab.ui.core.theme.AppTheme

@Composable
fun SensorsWidget(
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit,
    uiData: CustomWidgetUi.SensorsList,
    onToggle: (id: String) -> Unit,
) {
    val listState = rememberLazyListState()
    LazyColumn(
        modifier = modifier.height(500.dp),
        state = listState,
        contentPadding = PaddingValues(15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        stickyHeader {
            header()
        }
        items(uiData.sensors, key = { it.id }) {
            SensorCardRow(
                uiData = it, onToggle = { onToggle(it.id) })
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun SensorsWidgetPreview() {
    AppTheme {
        var data by remember {
            mutableStateOf(buildList {
                for (i in 1..30) {
                    add(
                        SensorCardUi.Widget.Row(
                            title = "Preview$i",
                            id = "Id$i",
                            state = SensorCardState.entries[(0..2).random()],
                            domain = "PreviewDomain$i",
                            drawableResource = SensorCardRes.lightBulb,
                            tints = SensorCardTints.Common.LightBulb
                        )
                    )
                }
            })
        }
        SensorsWidget(
            uiData = CustomWidgetUi.SensorsList(
                sensors = data, id = 1
            ), onToggle = { id ->
                data = buildList {
                    data.forEach {
                        if (it.id == id) {
                            add(it.copy(state = if (it.state == SensorCardState.On) SensorCardState.Off else SensorCardState.On))
                        } else {
                            add(it)
                        }
                    }
                }
            },
            header = { Text("Preview widget") }
        )
    }
}