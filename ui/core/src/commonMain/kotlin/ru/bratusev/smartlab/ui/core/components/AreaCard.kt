package ru.bratusev.smartlab.ui.core.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.ui.core.models.AreaCardUi
import ru.bratusev.smartlab.ui.core.theme.AppTheme
import smartlaboratory.ui.core.generated.resources.Res
import smartlaboratory.ui.core.generated.resources.thermometer

@Composable
fun AreaCard(modifier: Modifier = Modifier, uiData: AreaCardUi) {
    Card(modifier = modifier) {
        AsyncImage(
            model = uiData.pictureUrl,
            modifier = Modifier.size(512.dp),
            // TODO: найти и сделать адекватный placeholder
            placeholder = painterResource(Res.drawable.thermometer),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = uiData.name,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

// Coil не работает в preview
@Composable
@Preview
private fun AreaCardPreview() {
    AppTheme {
        AreaCard(
            uiData = AreaCardUi(
                areaId = "previewId",
                floorId = null,
                humidity = null,
                labels = emptyList(),
                name = "ПревьюИмя",
                pictureUrl = null,
                temperature = null,
                createdAt = 0.0,
                modifiedAt = 0.0
            )
        )
    }
}