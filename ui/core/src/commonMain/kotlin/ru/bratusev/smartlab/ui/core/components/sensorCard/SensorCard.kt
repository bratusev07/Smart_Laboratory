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
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardState
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardTints
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi
import ru.bratusev.smartlab.ui.core.theme.AppTheme
import smartlaboratory.ui.core.generated.resources.Res
import smartlaboratory.ui.core.generated.resources.light_bulb

@Composable
fun SensorCard(modifier: Modifier = Modifier, sensorCardUi: SensorCardUi, onClick: () -> Unit) {
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
            is SensorCardUi.Small -> {
                SmallCardContent(sensorCardUi = sensorCardUi)
            }

            is SensorCardUi.Medium -> {
                MediumCardContent(sensorCardUi = sensorCardUi)
            }

            is SensorCardUi.Large -> {
                LargeCardContent(sensorCardUi = sensorCardUi)
            }
        }
    }
}

@Composable
private fun SmallCardContent(sensorCardUi: SensorCardUi.Small) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconImage(
            sensorCardUi.drawableResource, sensorCardUi.state,
            tints = sensorCardUi.tints
        )
    }
}

@Composable
private fun MediumCardContent(sensorCardUi: SensorCardUi.Medium) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconImage(
            sensorCardUi.drawableResource, sensorCardUi.state,
            tints = sensorCardUi.tints
        )
        Text(
            text = sensorCardUi.title,
            modifier = Modifier.fillMaxWidth().padding(4.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun LargeCardContent(sensorCardUi: SensorCardUi.Large) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconImage(
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
private fun IconImage(
    drawableRes: DrawableResource,
    state: SensorCardState,
    tints: SensorCardTints,
) {
    val tint = when (state) {
        SensorCardState.On -> tints.on
        SensorCardState.Off -> tints.off
        SensorCardState.Unavailable -> tints.unavailable
    }

    Image(
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
    val small = SensorCardUi.Small(
        id = "0",
        state = SensorCardState.Off,
        domain = "switch",
        drawableResource = Res.drawable.light_bulb,
        tints = SensorCardTints.Common.LightBulb
    )

    AppTheme {
        SensorCard(sensorCardUi = small, onClick = {})
    }
}

@Preview(
    widthDp = 300, heightDp = 300
)
@Composable
private fun MediumCardPreview() {
    val medium = SensorCardUi.Medium(
        id = "1",
        state = SensorCardState.On,
        domain = "switch",
        title = "Свет 208",
        drawableResource = Res.drawable.light_bulb,
        tints = SensorCardTints.Common.LightBulb
    )

    AppTheme {
        SensorCard(sensorCardUi = medium, onClick = {})
    }
}

@Preview(
    widthDp = 300, heightDp = 300
)
@Composable
private fun LargeCardPreview() {
    val large = SensorCardUi.Large(
        id = "2",
        state = SensorCardState.Unavailable,
        domain = "switch",
        title = "Давление",
        description = "200 Па",
        drawableResource = Res.drawable.light_bulb,
        tints = SensorCardTints.Common.LightBulb
    )

    AppTheme {
        SensorCard(sensorCardUi = large, onClick = {})
    }
}