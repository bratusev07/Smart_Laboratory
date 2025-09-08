package ru.bratusev.smartlab.ui.core.components.sensorCard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardRes
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardTints
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorDomain
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorState
import ru.bratusev.smartlab.ui.core.theme.AppTheme

@Composable
fun SensorCardTile(
    modifier: Modifier = Modifier,
    sensorCardUi: SensorCardUi.Tile,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.fillMaxSize().aspectRatio(1f),
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurface,
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 5.dp, pressedElevation = 1.dp
        )
    ) {
        when (sensorCardUi) {
            is SensorCardUi.Tile.Small -> {
                SmallCardContent(sensorCardUi = sensorCardUi)
            }

            is SensorCardUi.Tile.Medium -> {
                MediumCardContent(sensorCardUi = sensorCardUi)
            }

            is SensorCardUi.Tile.Large -> {
                LargeCardContent(sensorCardUi = sensorCardUi)
            }

            is SensorCardUi.Tile.Sensor -> {
                SensorContent(sensorCardUi = sensorCardUi)
            }
        }
    }
}

@Composable
private fun SmallCardContent(sensorCardUi: SensorCardUi.Tile.Small) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SensorCardIconImage(
            sensorCardUi.drawableResource, sensorCardUi.state, tints = sensorCardUi.tints
        )
    }
}

@Composable
private fun MediumCardContent(sensorCardUi: SensorCardUi.Tile.Medium) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SensorCardIconImage(
            sensorCardUi.drawableResource, sensorCardUi.state, tints = sensorCardUi.tints
        )
        Text(
            text = sensorCardUi.title,
            modifier = Modifier.fillMaxWidth().padding(4.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SensorContent(sensorCardUi: SensorCardUi.Tile.Sensor) {
    println(sensorCardUi)
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SensorCardIconImage(
            sensorCardUi.drawableResource, sensorCardUi.state, tints = sensorCardUi.tints
        )
        Text(
            text = sensorCardUi.title,
            modifier = Modifier.fillMaxWidth().padding(4.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "${sensorCardUi.state.value} ${sensorCardUi.measurementUnit}",
            modifier = Modifier.fillMaxWidth().padding(4.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun LargeCardContent(sensorCardUi: SensorCardUi.Tile.Large) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SensorCardIconImage(
            sensorCardUi.drawableResource, sensorCardUi.state, tints = sensorCardUi.tints
        )
        Text(
            text = sensorCardUi.title,
            modifier = Modifier.fillMaxWidth().padding(4.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = sensorCardUi.description,
            modifier = Modifier.fillMaxWidth().padding(4.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
internal fun SensorCardIconImage(
    drawableRes: DrawableResource,
    state: SensorState,
    tints: SensorCardTints,
    modifier: Modifier = Modifier,
) {
    val tint = when (state) {
        SensorState.On -> tints.on
        SensorState.Off -> tints.off
        SensorState.Unavailable -> tints.unavailable
        is SensorState.SensorValue -> tints.unavailable
    }

    Image(
        modifier = modifier,
        painter = painterResource(drawableRes),
        contentDescription = null,
        colorFilter = ColorFilter.tint(tint),
    )
}

@Preview(
    widthDp = 300, heightDp = 300
)
@Composable
private fun SmallCardPreview() {
    val small = SensorCardUi.Tile.Small(
        id = "0",
        state = SensorState.Off,
        domain = SensorDomain.SWITCH,
        drawableResource = SensorCardRes.lightBulb,
        tints = SensorCardTints.Common.LightBulb
    )

    AppTheme {
        SensorCardTile(sensorCardUi = small, onClick = {})
    }
}

@Preview(
    widthDp = 300, heightDp = 300
)
@Composable
private fun MediumCardPreview() {
    val medium = SensorCardUi.Tile.Medium(
        id = "1",
        state = SensorState.On,
        domain = SensorDomain.SWITCH,
        title = "Свет 208",
        drawableResource = SensorCardRes.lightBulb,
        tints = SensorCardTints.Common.LightBulb
    )

    AppTheme {
        SensorCardTile(sensorCardUi = medium, onClick = {})
    }
}

@Preview(
    widthDp = 300, heightDp = 300
)
@Composable
private fun LargeCardPreview() {
    val large = SensorCardUi.Tile.Large(
        id = "2",
        state = SensorState.Unavailable,
        domain = SensorDomain.SWITCH,
        title = "Давление",
        description = "200 Па",
        drawableResource = SensorCardRes.lightBulb,
        tints = SensorCardTints.Common.LightBulb
    )

    AppTheme {
        SensorCardTile(sensorCardUi = large, onClick = {})
    }
}