package ru.bratusev.smartlab.ui.core.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.ui.core.models.SensorCardUi
import ru.bratusev.smartlab.ui.core.theme.AppTheme
import smartlaboratory.ui.core.generated.resources.Res
import smartlaboratory.ui.core.generated.resources.light_bulb

@Composable
fun SensorCardComponent(
    modifier: Modifier = Modifier,
    sensorCardUi: SensorCardUi,
    onCardClick: (String) -> Unit
) {
    Card(
        modifier.clickable(enabled = sensorCardUi.domain != "unavailable") { onCardClick(sensorCardUi.id) },
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurface,
        ),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize()
        ) {
            when (sensorCardUi) {
                is SensorCardUi.Small -> SmallCardComponent(sensorCardUi = sensorCardUi)
                is SensorCardUi.Medium -> MediumCardComponent(sensorCardUi = sensorCardUi)
                is SensorCardUi.Large -> LargeCardComponent(sensorCardUi = sensorCardUi)
            }
        }
    }
}

@Composable
fun SmallCardComponent(sensorCardUi: SensorCardUi.Small) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        IconImage(sensorCardUi.drawableResource, sensorCardUi.state)
    }
}

@Composable
fun MediumCardComponent(sensorCardUi: SensorCardUi.Medium) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IconImage(sensorCardUi.drawableResource, sensorCardUi.state)
        Text(
            text = sensorCardUi.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LargeCardComponent(sensorCardUi: SensorCardUi.Large) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconImage(sensorCardUi.drawableResource, sensorCardUi.state)
        Text(
            text = sensorCardUi.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = sensorCardUi.description,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun IconImage(
    drawableRes: DrawableResource,
    state: String
) {
    val tint = when (state.lowercase()) {
        "on" -> Color(red = 23, green = 23, blue = 180)
        "off" -> Color(red = 226, green = 211, blue = 28)
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    }

    Image(
        painter = painterResource(drawableRes),
        contentDescription = null,
        colorFilter = ColorFilter.tint(tint),
    )
}

@Preview(showBackground = true)
@Composable
private fun SensorCardComponentPreview() {
    val small = SensorCardUi.Small(id = "0", state = "off", domain = "switch", drawableResource = Res.drawable.light_bulb)
    val medium = SensorCardUi.Medium(id = "1", state = "on", domain = "switch", title = "Свет 208", drawableResource = Res.drawable.light_bulb)
    val large = SensorCardUi.Large(id = "2", state = "unavailable", domain = "switch", title = "Давление", description = "200 Па", drawableResource = Res.drawable.light_bulb)

    val cardModifier: Modifier = Modifier.size(140.dp)

    val sensors = listOf<SensorCardUi>(small, small, medium, small, large, small, medium, large)

    AppTheme {
        LazyVerticalGrid(
            modifier = Modifier,
            columns = GridCells.FixedSize(140.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            stickyHeader {
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button({}) {
                        Text("TODO")
                    }
                }
            }

            items(sensors) {
                SensorCardComponent(cardModifier, it) {  }
            }
        }
    }
}