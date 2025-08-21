package ru.bratusev.smartlab.ui.core.components.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.ui.core.components.sensorCard.SensorCardRow
import ru.bratusev.smartlab.ui.core.models.CustomWidgetUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardRes
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardTints
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorState
import ru.bratusev.smartlab.ui.core.theme.AppTheme

@Composable
fun SensorsWidget(
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit,
    uiData: CustomWidgetUi.SensorsList,
    onToggle: (id: String, newState: SensorState) -> Unit,
) {
    Column(
        modifier = modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        header()
        uiData.sensors.forEach {
            SensorCardRow(
                uiData = it, onToggle = {
                    onToggle(
                        it.id, if (it.state == SensorState.On) SensorState.Off else SensorState.On
                    )
                })
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun SensorsWidgetPreview() {
    AppTheme {
        val data = buildList {
            for (i in 1..30) {
                add(
                    SensorCardUi.Widget.Switchs(
                        title = "Preview$i",
                        id = "Id$i",
                        state = SensorState.entries[(0..2).random()],
                        domain = "PreviewDomain$i",
                        drawableResource = SensorCardRes.lightBulb,
                        tints = SensorCardTints.Common.LightBulb
                    )
                )
            }
        }
        SensorsWidget(
            uiData = CustomWidgetUi.SensorsList(
                sensors = data, index = 1
            ), onToggle = { _, _ -> {} },
            header = { Text("Preview widget") }
        )
    }
}