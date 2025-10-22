package ru.bratusev.smartlab.feature_area

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import ru.bratusev.smartlab.ui.core.resources.StringsRes

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
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Item 1: The AreaCard
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

        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(16.dp),
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ) {
            if (!state.value.areaDevices.isEmpty()) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.value.areaDevices.forEach { deviceUiData ->
                        SensorCardRow(
                            uiData = deviceUiData
                        )
                    }
                }
            } else {
                AnimatedVisibility(!state.value.areaDevices.isEmpty()) {
                    Column {
                        CircularProgressIndicator()
                        Text(StringsRes.LOADING_INDICATOR)
                    }
                }
            }
        }
    }
}