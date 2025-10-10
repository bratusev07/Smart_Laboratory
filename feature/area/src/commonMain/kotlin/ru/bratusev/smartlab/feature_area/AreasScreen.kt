package ru.bratusev.smartlab.feature_area

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import ru.bratusev.smartlab.feature_area.models.Event
import ru.bratusev.smartlab.ui.core.components.sensorCard.SensorCardRow
import ru.bratusev.smartlab.ui.core.models.AreaCardUi

@Composable
fun AreaScreen(
    areaId: String,
    areaScreenViewModel: AreaScreenViewModel = koinViewModel(),
) {
    val state = areaScreenViewModel.uiState.collectAsState()
    areaScreenViewModel.handleEvent(Event.LoadDevices(areaId))

    Column(Modifier.fillMaxSize()) {
        Text("Area id: $areaId")

        LazyColumn(modifier = Modifier.padding(horizontal = 5.dp, vertical = 4.dp)) {
            items(state.value.areaDevices) {
                Spacer(Modifier.height(4.dp))
                SensorCardRow(uiData = it)
            }
        }
    }
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