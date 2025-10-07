package ru.bratusev.smartlab.feature_area

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.ui.core.models.AreaCardUi
import ru.bratusev.smartlab.ui.core.theme.AppTheme

@Composable
fun AreaScreen(
    area: AreaCardUi,
) {
    Text("Area id: ${area.areaId}")
}

@Preview(
    showBackground = true
)
@Composable
private fun AreaScreenPreview() {
    val previewArea = AreaCardUi(
        areaId = "preview1Id",
        name = "Preview1",
        floorId = null,
        labels = emptyList(),
        humidity = null,
        temperature = null,
        pictureUrl = null,
        createdAt = -1.0,
        modifiedAt = -1.0
    )
    AppTheme {
        AreaScreen(area = previewArea)
    }
}