package ru.bratusev.smartlab.ui.core.components.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.bratusev.smartlab.ui.core.components.modals.FindSingleSensorModal
import ru.bratusev.smartlab.ui.core.components.sensorCard.SensorCardTile
import ru.bratusev.smartlab.ui.core.models.CustomWidgetUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorDomain
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleSensorWidget(
    modifier: Modifier = Modifier,
    uiData: CustomWidgetUi.SingleSensor,
    onToggle: (id: String, newState: SensorState) -> Unit,
    onSubmit: (selectedId: String) -> Unit,
    onEditEnd: () -> Unit
) {
    if (uiData.openModal) {
        FindSingleSensorModal(
            sensors = uiData.sensorsToChooseFrom,
            filterDomain = SensorDomain.SWITCH,
            onClose = onEditEnd,
            onSubmit = {
                onEditEnd()
                onSubmit(it)
            })
    }
    Column(
        modifier = modifier, verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        SensorCardTile(
            sensorCardUi = SensorCardUi.Tile.Medium(
                title = uiData.sensor.title.ifEmpty { uiData.sensor.id },
                id = uiData.sensor.id,
                state = uiData.sensor.state,
                domain = uiData.sensor.domain,
                drawableResource = uiData.sensor.drawableResource,
                tints = uiData.sensor.tints
            ),
            onClick = {
                onToggle(
                    uiData.sensor.id,
                    if (uiData.sensor.state == SensorState.On) SensorState.Off else SensorState.On
                )
            }
        )
    }
}

