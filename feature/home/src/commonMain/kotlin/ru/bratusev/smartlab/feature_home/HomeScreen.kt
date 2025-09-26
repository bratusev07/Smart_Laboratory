package ru.bratusev.smartlab.feature_home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.context.startKoin
import ru.bratusev.smartlab.feature_home.models.Event
import ru.bratusev.smartlab.navigation.api.NavigationApi
import ru.bratusev.smartlab.navigation.api.Screen
import ru.bratusev.smartlab.ui.core.components.sensorCard.SensorCardGridPager
import ru.bratusev.smartlab.ui.core.theme.AppTheme

@Composable
fun HomeScreen(
    vm: HomeViewModel = koinViewModel(),
    navigationApi: NavigationApi,
) {
    val state = vm.uiState.collectAsState()
    if (state.value.sensorCardGridPagerUiData.sensors.isNotEmpty()) {
        SensorCardGridPager(
            uiData = state.value.sensorCardGridPagerUiData,
            onSensorCardClicked = { vm.handleEvent(Event.OnSwitchUpdated(it)) }
        )
    }
}


@Preview
@Composable
private fun HomeScreenPreview() {
    startKoin {
        modules(homeModulePreview)
    }
    AppTheme {
        HomeScreen(
            navigationApi = object : NavigationApi {
                override fun navigateTo(screen: Screen) {}
                override fun navigateToHome() {}
                override fun navigateToLogin() {}
                override fun navigateToSettings() {}
                override fun navigateToLogcat() {}
                override fun navigateToAreasScreen() {}
                override fun navigateToAddWidgetCustomScreen() {}
                override fun popBackStack() {}
            }
        )
    }
}
