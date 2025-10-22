package ru.bratusev.smartlab.feature_area

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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

    LaunchedEffect(areaId) {
        areaScreenViewModel.handleEvent(Event.FetchData(areaId))
    }

    // Use a single LazyColumn for the entire screen content.
    // This is more efficient and handles scrolling for all elements together.
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        // Apply consistent padding for all content within the list.
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
        // Apply consistent spacing between all items in the LazyColumn.
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Item 1: The AreaCard
        item {
            AreaCard(
                onClick = { _, _, _ -> { /* Handle click */ } },
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
        }

        // Items 2...N: The list of sensor devices
        items(state.value.areaDevices) { deviceUiData ->
            SensorCardRow(uiData = deviceUiData)
        }
    }
}