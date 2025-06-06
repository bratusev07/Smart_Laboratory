package ru.bratusev.smartlab.feature_settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel
import ru.bratusev.smartlab.feature_settings.models.Event
import ru.bratusev.smartlab.ui.core.components.CustomButton
import ru.bratusev.smartlab.ui.core.models.CustomButtonUi

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit
) {
    val vm = SettingsViewModel()
    val state = vm.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        CustomButton(
            modifier = Modifier.align(Alignment.Center),
            customButtonUi = CustomButtonUi(state.value.screenName, 300)
        ) {
            onNavigateBack()
        }
    }
}