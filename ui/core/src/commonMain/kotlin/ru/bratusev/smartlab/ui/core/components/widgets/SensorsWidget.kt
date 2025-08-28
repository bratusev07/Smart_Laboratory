package ru.bratusev.smartlab.ui.core.components.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.ui.core.components.WidgetToolBar
import ru.bratusev.smartlab.ui.core.components.modals.FindSensorModal
import ru.bratusev.smartlab.ui.core.components.sensorCard.SensorCardRow
import ru.bratusev.smartlab.ui.core.models.CustomWidgetUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardRes
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardTints
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorDomain
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorState
import ru.bratusev.smartlab.ui.core.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SensorsWidget(
    modifier: Modifier = Modifier,
    uiData: CustomWidgetUi.SensorsList,
    onToggle: (id: String, newState: SensorState) -> Unit,
    onSubmit: (chosenIds: List<String>) -> Unit,
    onDeleteWidgetClick: () -> Unit,
) {
    var isModalOpen by remember { mutableStateOf(false) }
    if (isModalOpen) {
        FindSensorModal(
            sensors = uiData.sensorsToChooseFrom,
            filterDomain = SensorDomain.SWITCH,
            onClose = { isModalOpen = false },
            onSubmit = onSubmit
        )
    }
    Column(
        modifier = modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        WidgetToolBar(
            title = "Виджет с id: ${uiData.id}",
            onEditClick = {
                isModalOpen = true
            },
            onAddClick = {},
            showDeleteButton = uiData.showDeleteButton,
            onDeleteClick = onDeleteWidgetClick
        )
        uiData.sensorsToShow.forEach {
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
                    SensorCardUi.Widget.Switches(
                        title = "Preview$i",
                        id = "Id$i",
                        state = SensorState.entries[(0..2).random()],
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
                        state = SensorState.entries[(0..2).random()],
                        domain = SensorDomain.SWITCH,
                        drawableResource = SensorCardRes.lightBulb,
                        tints = SensorCardTints.Common.LightBulb
                    )
                )
            }
        }
        SensorsWidget(
            uiData = CustomWidgetUi.SensorsList(
                sensorsToShow = data, id = 1,
                sensorsToChooseFrom = data2,
            ), onToggle = { _, _ -> {} },
            onSubmit = {},
            onDeleteWidgetClick = {}
        )
    }
}