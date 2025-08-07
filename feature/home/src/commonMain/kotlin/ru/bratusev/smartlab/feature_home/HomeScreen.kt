package ru.bratusev.smartlab.feature_home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.context.startKoin
import ru.bratusev.smartlab.domain.core.model.socket.ServiceEntity
import ru.bratusev.smartlab.feature_home.models.Event
import ru.bratusev.smartlab.navigation.api.NavigationApi
import ru.bratusev.smartlab.navigation.api.Screen
import ru.bratusev.smartlab.ui.core.theme.AppTheme

@Composable
fun HomeScreen(
    vm: HomeViewModel = koinViewModel(),
    navigationApi: NavigationApi,
) {
    val state = vm.uiState.collectAsState()

    LazyColumn(
        Modifier.fillMaxSize()
    ) {
        items(state.value.switchesEntity) {
            ServiceEntityItemUi(it) { id ->
                vm.handleEvent(Event.OnSwitchUpdated(id))
            }
        }
    }
}

@Composable
fun ServiceEntityItemUi(serviceEntity: ServiceEntity, onServiceSwitched: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .background(Color(10, 30, 40, 124))
            .clickable(enabled = serviceEntity.state?.contains("unavailable") != true) {
                serviceEntity.id?.let { onServiceSwitched(it) }
            }
    ) {
        Text(text = serviceEntity.id.toString(), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = serviceEntity.state.toString(), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = serviceEntity.attributes.toString(), modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun ErrorItemUi(message: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(180, 20, 20, 90))
    ) {
        Text(text = message, color = Color.White)
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
