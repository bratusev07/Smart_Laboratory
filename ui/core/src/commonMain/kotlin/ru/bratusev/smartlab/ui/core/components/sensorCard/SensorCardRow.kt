package ru.bratusev.smartlab.ui.core.components.sensorCard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardRes
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardTints
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorState
import ru.bratusev.smartlab.ui.core.theme.AppTheme

@Composable
fun SensorCardRow(
    modifier: Modifier = Modifier,
    uiData: SensorCardUi.Widget.Switches,
    onToggle: () -> Unit,
) {
    val currentState = uiData.state == SensorState.On
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            SensorCardIconImage(
                modifier = Modifier.height(35.dp),
                drawableRes = uiData.drawableResource, state = uiData.state, tints = uiData.tints
            )
            Text("${uiData.title} ${uiData.id}", style = MaterialTheme.typography.titleMedium)
        }
        Switch(
            modifier = Modifier.padding(start = 15.dp),
            enabled = uiData.state != SensorState.Unavailable,
            checked = currentState,
            onCheckedChange = { onToggle() })
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun SensorCardRowPreview() {
    var currentState by remember { mutableStateOf(SensorState.On) }
    AppTheme {
        SensorCardRow(
            uiData = SensorCardUi.Widget.Switches(
                title = "Маленькая лампочка",
                id = "0",
                state = currentState,
                domain = "switch",
                drawableResource = SensorCardRes.lightBulb,
                tints = SensorCardTints.Common.LightBulb,
            ), onToggle = { })
    }
}
