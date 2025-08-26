package ru.bratusev.smartlab.ui.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.ui.core.components.widgets.SensorsWidget
import ru.bratusev.smartlab.ui.core.models.CustomWidgetEvent
import ru.bratusev.smartlab.ui.core.models.CustomWidgetUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardRes
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardTints
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorDomain
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorState
import ru.bratusev.smartlab.ui.core.theme.AppTheme

@Composable
fun CustomWidget(uiData: CustomWidgetUi, onEvent: (event: CustomWidgetEvent) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().background(
            shape = RoundedCornerShape(30.dp), color = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        when (uiData) {
            is CustomWidgetUi.SensorsList -> SensorsWidget(
                uiData = uiData,
                onToggle = { sensorId, newState ->
                    onEvent(CustomWidgetEvent.SensorStateChange(sensorId, newState))
                },
                onSubmit = { chosenIds ->
                    onEvent(
                        CustomWidgetEvent.ChosenSwitchesChange(
                            chosenIds
                        )
                    )
                })
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun SensorListPreview() {
    AppTheme {
        val data = buildList {
            for (i in 1..30) {
                add(
                    SensorCardUi.Widget.Switches(
                        title = "Preview$i",
                        id = "Id$i",
                        state = SensorState.entries[(0..2).random()],
                        domain = SensorDomain.entries.random(),
                        drawableResource = SensorCardRes.lightBulb,
                        tints = SensorCardTints.Common.LightBulb
                    )
                )
            }
        }
        val data2 = buildList {
            for (i in 1..30) {
                add(
                    SensorCardUi.Modal(
                        title = "Preview$i",
                        id = "Id$i",
                        state = SensorState.entries[(0..2).random()],
                        domain = SensorDomain.entries.random(),
                        drawableResource = SensorCardRes.lightBulb,
                        tints = SensorCardTints.Common.LightBulb
                    )
                )
            }
        }
        CustomWidget(
            uiData = CustomWidgetUi.SensorsList(
                sensorsToShow = data, id = 1, sensorsToChooseFrom = data2
            ), onEvent = {})
    }
}
