package ru.bratusev.smartlab.feature_home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.context.startKoin
import ru.bratusev.smartlab.feature_home.mappers.mapToServiceListUi
import ru.bratusev.smartlab.navigation.api.NavigationApi
import ru.bratusev.smartlab.navigation.api.Screen
import ru.bratusev.smartlab.ui.core.components.sensorCard.SensorCardVerticalGrid
import ru.bratusev.smartlab.ui.core.theme.AppTheme

@Composable
fun HomeScreen(
    vm: HomeViewModel = koinViewModel(),
    navigationApi: NavigationApi,
) {
    val state = vm.uiState.collectAsState()

    if(state.value.switchesEntity.isNotEmpty()) {
        SensorCardVerticalGrid(
            modifier = Modifier.fillMaxSize().padding(vertical = 6.dp, horizontal = 14.dp),
            uiData = state.value.switchesEntity.mapToServiceListUi(),
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
                override fun popBackStack() {}
            }
        )
    }
}
