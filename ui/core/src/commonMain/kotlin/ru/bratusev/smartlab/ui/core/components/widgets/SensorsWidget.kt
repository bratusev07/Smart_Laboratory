package ru.bratusev.smartlab.ui.core.components.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.ui.core.components.modals.FindManySensorModal
import ru.bratusev.smartlab.ui.core.components.sensorCard.SensorCardRow
import ru.bratusev.smartlab.ui.core.models.CustomWidgetUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardRes
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardTints
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorDomain
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorState
import ru.bratusev.smartlab.ui.core.theme.AppTheme
import smartlaboratory.ui.core.generated.resources.Res
import smartlaboratory.ui.core.generated.resources.use_edit_mode_to_add_elements

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManySensorsWidget(
    modifier: Modifier = Modifier,
    uiData: CustomWidgetUi.ManySensorsList,
    onToggle: (id: String, newState: SensorState) -> Unit,
    onSubmit: (chosenIds: List<String>) -> Unit,
    onEditEnd: () -> Unit
) {
    if (uiData.openModal) {
        FindManySensorModal(
            sensors = uiData.sensorsToChooseFrom,
            filterDomain = SensorDomain.SWITCH,
            onClose = onEditEnd,
            isAddMode = uiData.isAddMode,
            onSubmit = {
                onEditEnd()
                onSubmit(it)
            },
            currentSensorsIds = uiData.sensorsToShow.map { it.id },
        )
    }
    Column(
        modifier = modifier, verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        if (uiData.sensorsToShow.isEmpty()) {
            Text(stringResource(Res.string.use_edit_mode_to_add_elements))
        } else {
            uiData.sensorsToShow.forEach { sensor ->
                SensorCardRow(
                    uiData = SensorCardUi.Row(
                        title = sensor.title,
                        id = sensor.id,
                        state = sensor.state,
                        domain = sensor.domain,
                        drawableResource = sensor.drawableResource,
                        tints = sensor.tints
                    ), buttonContent = {
                        Switch(
                            modifier = Modifier.padding(start = 15.dp),
                            enabled = sensor.state != SensorState.Unavailable,
                            checked = sensor.state == SensorState.On,
                            onCheckedChange = {
                                onToggle(
                                    sensor.id,
                                    if (sensor.state == SensorState.On) SensorState.Off else SensorState.On
                                )
                            })
                    })
            }
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
                    SensorCardUi.Widget.Switch(
                        title = "Preview$i",
                        id = "Id$i",
                        state = SensorState.On,
                        domain = SensorDomain.SWITCH,
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
                        state = SensorState.On,
                        domain = SensorDomain.SWITCH,
                        drawableResource = SensorCardRes.lightBulb,
                        tints = SensorCardTints.Common.LightBulb
                    )
                )
            }
        }
        ManySensorsWidget(
            uiData = CustomWidgetUi.ManySensorsList(
                sensorsToShow = data, id = 1,
                sensorsToChooseFrom = data2,
                openModal = false,
            ), onToggle = { _, _ -> {} }, onSubmit = {}, onEditEnd = {})
    }
}