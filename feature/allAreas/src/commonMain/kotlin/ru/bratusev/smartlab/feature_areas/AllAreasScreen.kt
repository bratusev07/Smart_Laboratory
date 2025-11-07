package ru.bratusev.smartlab.feature_areas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.ui.core.components.AreaCard
import ru.bratusev.smartlab.ui.core.components.LoadingIndicator
import ru.bratusev.smartlab.ui.core.models.AreaCardUi
import ru.bratusev.smartlab.ui.core.theme.AppTheme

@Composable
fun AllAreasScreen(
    areas: List<AreaCardUi>,
    navigateToArea: (areaId: String, friendlyName: String?, pictureUrl: String?) -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        stickyHeader {
            LoadingIndicator(show = areas.isEmpty())
        }
        items(areas, key = { it.areaId }) {
            AreaCard(uiData = it, onClick = navigateToArea)
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun AreasScreenPreview() {
    val previewAreasList = buildList {
        add(
            AreaCardUi(
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
        )
        add(
            AreaCardUi(
                areaId = "preview2Id",
                name = "Preview2",
                floorId = null,
                labels = emptyList(),
                humidity = null,
                temperature = null,
                pictureUrl = null,
                createdAt = -1.0,
                modifiedAt = -1.0
            )
        )
        add(
            AreaCardUi(
                areaId = "preview3Id",
                name = "Preview3",
                floorId = null,
                labels = emptyList(),
                humidity = null,
                temperature = null,
                pictureUrl = null,
                createdAt = -1.0,
                modifiedAt = -1.0
            )
        )
        add(
            AreaCardUi(
                areaId = "preview4Id",
                name = "Preview4",
                floorId = null,
                labels = emptyList(),
                humidity = null,
                temperature = null,
                pictureUrl = null,
                createdAt = -1.0,
                modifiedAt = -1.0
            )
        )
        add(
            AreaCardUi(
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
        )
    }
    AppTheme {
        AllAreasScreen(
            areas = previewAreasList,
            navigateToArea = { _, _, _ -> {} }
        )
    }
}