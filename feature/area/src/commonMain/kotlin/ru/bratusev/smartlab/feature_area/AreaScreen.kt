package ru.bratusev.smartlab.feature_area

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ru.bratusev.smartlab.feature_area.models.Event
import ru.bratusev.smartlab.ui.core.components.AreaCard
import ru.bratusev.smartlab.ui.core.components.LoadingIndicator
import ru.bratusev.smartlab.ui.core.components.sensorCard.SensorCardRow
import ru.bratusev.smartlab.ui.core.models.AreaCardUi
import smartlaboratory.ui.core.generated.resources.Res
import smartlaboratory.ui.core.generated.resources.loading

@Composable
fun AreaScreen(
    areaId: String,
    friendlyName: String?,
    pictureUrl: String?,
    vm: AreaScreenViewModel = koinViewModel(),
) {
    val state = vm.uiState.collectAsState()

    LaunchedEffect(areaId) {
        vm.handleEvent(Event.FetchData(areaId))
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AreaCard(
            onClick = { _, _, _ -> { /* Handle click */ } }, uiData = AreaCardUi(
                areaId = areaId,
                name = friendlyName ?: areaId,
                floorId = null,
                labels = emptyList(),
                humidity = null,
                temperature = null,
                pictureUrl = pictureUrl,
                createdAt = -1.0,
                modifiedAt = -1.0,
                isClickable = false
            )
        )

        if (!state.value.areaDevices.isEmpty()) {
            Column(
                modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.value.areaDevices.forEach { deviceUiData ->
                    SensorCardRow(
                        uiData = deviceUiData
                    )
                }
            }
        } else {
            LoadingIndicator(
                !state.value.areaDevices.isEmpty(), stringResource(Res.string.loading),
                onTimeOut = { vm.handleEvent(Event.OnLoadingTimeOut) }
            )
        }
    }
}