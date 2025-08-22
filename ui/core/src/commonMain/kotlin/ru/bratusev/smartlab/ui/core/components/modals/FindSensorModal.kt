package ru.bratusev.smartlab.ui.core.components.modals

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorDomain

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindSensorModal(
    sensors: List<SensorCardUi.Modal>,
    domain: SensorDomain,
    sheetState: SheetState = rememberModalBottomSheetState(),
    onClose: () -> Unit,
) {
    val filteredSensors by remember(sensors, domain) {
        derivedStateOf {
            sensors.filter { sensor -> sensor.domain.equals(domain.name, ignoreCase = true) }
        }
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onClose,
    ) {
        LazyColumn {
            items(filteredSensors) {
                SensorModalItem(it)
            }
        }
    }
}

@Composable
private fun SensorModalItem(sensor: SensorCardUi.Modal) {
    Row {
        Text(sensor.title ?: "title empty")
        Text(sensor.domain)
        Text(sensor.state.name)
        Text(sensor.id)
    }
}