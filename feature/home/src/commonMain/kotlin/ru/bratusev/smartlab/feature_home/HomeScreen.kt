package ru.bratusev.smartlab.feature_home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import org.koin.compose.viewmodel.koinViewModel
import ru.bratusev.smartlab.feature_home.models.Event
import ru.bratusev.smartlab.navigation.api.NavigationApi
import ru.bratusev.smartlab.ui.core.components.LoadingIndicator
import ru.bratusev.smartlab.ui.core.components.sensorCard.SensorCardGridPager

@Composable
fun HomeScreen(
    vm: HomeViewModel = koinViewModel(),
    navigationApi: NavigationApi,
) {
    val state = vm.uiState.collectAsState()
    LoadingIndicator(state.value.isUpdating)
    if (state.value.sensorCardGridPagerUiData.sensors.isNotEmpty()) {
        SensorCardGridPager(
            uiData = state.value.sensorCardGridPagerUiData,
            onSensorCardClicked = { vm.handleEvent(Event.OnSwitchUpdated(it)) }
        )
    }
}