package ru.bratusev.smartlab.feature_automation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel
import ru.bratusev.smartlab.navigation.api.NavigationApi
import ru.bratusev.smartlab.ui.core.components.AutomationListComponent

@Composable
fun AutomationScreen(
    vm: AutomationViewModel = koinViewModel(),
    navigationApi: NavigationApi
) {
    val state = vm.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        AutomationListComponent(automationUi = state.value.automation) { automationId ->
            // TODO Add automation click logic
        }
    }
}

