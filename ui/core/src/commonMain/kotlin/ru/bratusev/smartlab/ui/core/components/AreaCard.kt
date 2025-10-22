package ru.bratusev.smartlab.ui.core.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun AreaCard(
    modifier: Modifier = Modifier,
    onClick: (String, String?, String?) -> Unit,
    uiData: AreaCardUi
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = { onClick(uiData.areaId, uiData.name, uiData.pictureUrl) },
        shape = RoundedCornerShape(16.dp), // A slightly larger corner radius can look more modern
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        // Use a Column to structure content inside the card
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val imageModifier = Modifier
                .fillMaxWidth()
                // Use aspectRatio to maintain a consistent shape (e.g., 16:9)
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))

            if (uiData.pictureUrl != null) {
                AsyncImage(
                    model = uiData.pictureUrl,
                    modifier = imageModifier,
                    // A placeholder from your resources
                    placeholder = painterResource(Res.drawable.thermometer),
                    error = painterResource(Res.drawable.thermometer), // Show an error image if loading fails
                    contentScale = ContentScale.Crop, // Crop is often better for filling space
                    contentDescription = "${uiData.name} image"
                )
            } else {
                // Fallback content when there is no picture URL
                Image(
                    painter = painterResource(Res.drawable.thermometer),
                    modifier = imageModifier.padding(32.dp), // Add padding to the placeholder
                    contentScale = ContentScale.Fit,
                    contentDescription = null
                )
            }

            // Spacer for better visual separation between image and text
            Spacer(Modifier.height(8.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp), // Add padding around the text
                text = uiData.name,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

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
                name = "Preview Name",
                pictureUrl = null,
                temperature = null,
                createdAt = 0.0,
                modifiedAt = 0.0
            ),
            onClick = { _, _, _ -> {} }
        )
    }
}