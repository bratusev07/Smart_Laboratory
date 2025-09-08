package ru.bratusev.smartlab.ui.core.components.modals

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.ui.core.components.sensorCard.SensorCardRow
import ru.bratusev.smartlab.ui.core.components.sensorCard.SensorCardRowLabel
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardRes
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorDomain
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorState
import ru.bratusev.smartlab.ui.core.theme.AppTheme
import ru.bratusev.smartlab.ui.core.theme.Colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindSingleSensorModal(
    sensors: List<SensorCardUi.Modal>,
    filterDomain: SensorDomain,
    sheetState: SheetState = rememberModalBottomSheetState(),
    onClose: () -> Unit,
    onSubmit: (selectedId: String) -> Unit,
) {
    val filteredSensors by remember(sensors, filterDomain) {
        derivedStateOf {
            sensors.filter { sensor -> sensor.domain == filterDomain }
        }
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onClose,
    ) {
        ModalBottomSheetContent(
            modifier = Modifier.padding(horizontal = 12.dp),
            sensors = filteredSensors,
            onSubmit = onSubmit
        )
    }
}

@Composable
private fun ModalBottomSheetContent(
    modifier: Modifier = Modifier,
    sensors: List<SensorCardUi.Modal>,
    onSubmit: (selectedId: String) -> Unit,
) {
    var selectedId by rememberSaveable { mutableStateOf("") }
    LazyColumn(modifier = modifier) {
        item {
            ModalToolBar(
                modifier = Modifier.padding(bottom = 24.dp),
                title = "Выберите из доступного",
                onSubmit = {
                    onSubmit(selectedId)
                })
        }
        items(sensors, key = { it.id }) { sensor ->
            SensorModalItem(
                modifier = Modifier.clickable { selectedId = sensor.id },
                sensor,
                checked = sensor.id == selectedId
            )
        }
    }
}

@Composable
private fun SensorModalItem(
    modifier: Modifier = Modifier,
    sensor: SensorCardUi.Modal,
    checked: Boolean,
) {
    SensorCardRow(
        modifier = modifier, uiData = SensorCardUi.Row(
            title = sensor.title ?: "",
            id = sensor.id,
            state = sensor.state,
            domain = sensor.domain,
            drawableResource = sensor.drawableResource,
            tints = sensor.tints
        ), buttonContent = {
            RadioButton(
                selected = checked, onClick = { })
        }, label = {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SensorCardRowLabel(
                    text = sensor.domain.localeName,
                )
                SensorCardRowLabel(
                    text = sensor.state.localeName,
                    borderColor = when (sensor.state) {
                        SensorState.On -> Colors.success
                        SensorState.Off -> MaterialTheme.colorScheme.errorContainer
                        SensorState.Unavailable -> MaterialTheme.colorScheme.outline
                    }
                )
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    showBackground = true
)
@Composable
private fun FindSenorModalContent() {
    val sensors = buildList {
        for (i in 1..30) {
            add(
                SensorCardUi.Modal(
                    state = SensorState.entries.random(),
                    title = "Preview",
                    id = "Id$i",
                    domain = SensorDomain.SWITCH,
                    drawableResource = SensorCardRes.lightBulb,
                )
            )
        }
    }
    AppTheme {
        ModalBottomSheetContent(
            sensors = sensors,
            onSubmit = {},
        )
    }
}