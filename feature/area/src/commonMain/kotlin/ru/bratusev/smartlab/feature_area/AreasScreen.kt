package ru.bratusev.smartlab.feature_area

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import ru.bratusev.smartlab.feature_area.models.Event
import ru.bratusev.smartlab.ui.core.components.AreaCard
import ru.bratusev.smartlab.ui.core.components.sensorCard.SensorCardRow
import ru.bratusev.smartlab.ui.core.models.AreaCardUi

@Composable
fun AreaScreen(
    areaId: String,
    friendlyName: String?,
    pictureUrl: String?,
    areaScreenViewModel: AreaScreenViewModel = koinViewModel(),
) {
    val state = areaScreenViewModel.uiState.collectAsState()

    LaunchedEffect(areaId) { // Changed Unit to areaId
        areaScreenViewModel.handleEvent(Event.FetchData(areaId))
    }

    Column(
        Modifier.fillMaxSize().padding(top = 24.dp, start = 12.dp, end = 12.dp),
        verticalArrangement = Arrangement.spacedBy(
            24.dp, Alignment.Top
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AreaCard(
            onClick = {_, _, _ -> {}},
            uiData = AreaCardUi(
                areaId = areaId,
                name = friendlyName ?: areaId,
                floorId = null,
                labels = emptyList(),
                humidity = null,
                temperature = null,
                pictureUrl = pictureUrl,
                createdAt = -1.0,
                modifiedAt = -1.0,
            )
        )

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