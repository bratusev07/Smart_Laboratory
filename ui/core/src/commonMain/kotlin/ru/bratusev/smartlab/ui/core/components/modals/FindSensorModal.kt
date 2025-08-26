package ru.bratusev.smartlab.ui.core.components.modals

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardRes
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorDomain
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorState
import ru.bratusev.smartlab.ui.core.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindSensorModal(
    sensors: List<SensorCardUi.Modal>,
    filterDomain: SensorDomain,
    sheetState: SheetState = rememberModalBottomSheetState(),
    onClose: () -> Unit,
    onSubmit: (checkedIds: List<String>) -> Unit,
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
        ModalBottomSheetContent(sensors = filteredSensors, onSubmit = onSubmit)
    }
}

@Composable
private fun ModalBottomSheetContent(
    modifier: Modifier = Modifier,
    sensors: List<SensorCardUi.Modal>,
    onSubmit: (List<String>) -> Unit,
) {
    val checkedIds = rememberSaveable { mutableStateListOf<String>() }
    LazyColumn(modifier = modifier) {
        item {
            ModalToolBar(
                onSubmit = {
                    println("checkedIds: $checkedIds")
                    onSubmit(checkedIds)
                }
            )
        }
        items(sensors, key = { it.id }) { sensor ->
            SensorModalItem(sensor, checked = sensor.id in checkedIds, onCheckedChange = {
                if (sensor.id in checkedIds) checkedIds.remove(sensor.id)
                else checkedIds.add(sensor.id)
            })
        }
    }
}

@Composable
private fun SensorModalItem(
    sensor: SensorCardUi.Modal,
    checked: Boolean,
    onCheckedChange: () -> Unit,
) {
    Row {
        Text(sensor.title ?: "title empty")
        Text(sensor.domain.name)
        Text(sensor.state.name)
        Text(sensor.id)
        Checkbox(
            checked = checked, onCheckedChange = { onCheckedChange() }
        )
    }
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