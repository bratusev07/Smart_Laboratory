package ru.bratusev.smartlab.ui.core.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.ui.core.models.TileButtonUi
import ru.bratusev.smartlab.ui.core.theme.AppTheme
import ru.bratusev.smartlab.ui.core.theme.TileButtonColors

@Composable
fun TileButton(modifier: Modifier = Modifier, tileButtonUi: TileButtonUi, onClick: () -> Unit) {
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
        when (tileButtonUi) {
            is TileButtonUi.LightBulb -> {
                LightBulbContent(tileButtonUi = tileButtonUi)
            }

            is TileButtonUi.Thermometer -> {
                ThermometerContent(tileButtonUi = tileButtonUi)
            }
        }
    }
}

@Composable
private fun LightBulbContent(tileButtonUi: TileButtonUi.LightBulb) {
    val colorTint = when {
        !tileButtonUi.isEnabled -> TileButtonColors.LightBulb.Disabled
        tileButtonUi.isOn -> TileButtonColors.LightBulb.On
        else -> TileButtonColors.LightBulb.Off
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(tileButtonUi.resource),
            colorFilter = ColorFilter.tint(colorTint),
            contentDescription = null
        )
        Text(
            modifier = Modifier.wrapContentHeight(),
            overflow = TextOverflow.Ellipsis,
            text = tileButtonUi.location,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ThermometerContent(tileButtonUi: TileButtonUi.Thermometer) {
    val colorTint = when {
        !tileButtonUi.isEnabled -> TileButtonColors.Thermometer.disabled
        tileButtonUi.temperature >= 30f -> TileButtonColors.Thermometer.veryHot
        tileButtonUi.temperature >= 15f -> TileButtonColors.Thermometer.hot
        else -> TileButtonColors.Thermometer.cold
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(tileButtonUi.resource),
            colorFilter = ColorFilter.tint(colorTint),
            contentDescription = null
        )
        Text(
            modifier = Modifier.wrapContentHeight(),
            overflow = TextOverflow.Visible,
            text = tileButtonUi.temperature.toString() + " ℃",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelLarge
        )
        Text(
            modifier = Modifier.wrapContentHeight(),
            overflow = TextOverflow.Ellipsis,
            text = tileButtonUi.location,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(
    group = "TileButton", name = "LightBulb", showBackground = true, widthDp = 250, heightDp = 250
)
@Composable
private fun TileButtonPreviewLightBulb() {
    AppTheme {
        TileButton(
            tileButtonUi = TileButtonUi.LightBulb(
                location = "Previesdfsdfw Preview", isOn = true
            ), onClick = {})
    }
}

