package ru.bratusev.smartlab.ui.core.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import ru.bratusev.smartlab.ui.core.models.TileButtonUi
import ru.bratusev.smartlab.ui.core.theme.TileButtonColors
import smartlaboratory.ui.core.generated.resources.Res
import smartlaboratory.ui.core.generated.resources.light_bulb
import smartlaboratory.ui.core.generated.resources.thermometer

@Composable
fun TileButton(modifier: Modifier = Modifier, tileButtonUi: TileButtonUi, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.aspectRatio(1f),
        colors = ButtonColors(
            containerColor = Color.White,
            contentColor = Color.Gray,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.Gray
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 5.dp,
            pressedElevation = 1.dp
        )
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
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
}

@Composable
private fun LightBulbContent(tileButtonUi: TileButtonUi.LightBulb) {
    val colorTint = when {
        !tileButtonUi.isEnabled -> TileButtonColors.LightBulb.Disabled
        tileButtonUi.isOn -> TileButtonColors.LightBulb.On
        else -> TileButtonColors.LightBulb.Off
    }
    Image(
        painter = painterResource(Res.drawable.light_bulb),
        colorFilter = ColorFilter.tint(colorTint),
        contentDescription = null
    )
    Text(text = tileButtonUi.location, textAlign = TextAlign.Center)
}

@Composable
fun ThermometerContent(tileButtonUi: TileButtonUi.Thermometer) {
    val colorTint = when {
        !tileButtonUi.isEnabled -> TileButtonColors.Thermometer.disabled
        tileButtonUi.temperature >= 30f -> TileButtonColors.Thermometer.veryHot
        tileButtonUi.temperature >= 15f -> TileButtonColors.Thermometer.hot
        else -> TileButtonColors.Thermometer.cold
    }
    Image(
        painter = painterResource(Res.drawable.thermometer),
        colorFilter = ColorFilter.tint(colorTint),
        contentDescription = null
    )
    Text(
        text = tileButtonUi.temperature.toString() + " ℃",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.labelLarge
    )
    Text(text = tileButtonUi.location, textAlign = TextAlign.Center)
}

