package ru.bratusev.smartlab.feature_area

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import ru.bratusev.smartlab.feature_area.models.Event
import ru.bratusev.smartlab.ui.core.models.AreaCardUi

@Composable
fun AreaScreen(
    areaId: String,
    areaScreenViewModel: AreaScreenViewModel = koinViewModel(),
) {
    areaScreenViewModel.handleEvent(Event.LoadDevices(areaId))
    Text("Area id: $areaId")
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
}