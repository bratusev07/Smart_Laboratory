package ru.bratusev.smartlab.feature_login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel
import ru.bratusev.smartlab.feature_login.models.Event
import ru.bratusev.smartlab.ui.core.components.CustomButton
import ru.bratusev.smartlab.ui.core.models.CustomButtonUi

@Composable
fun LoginScreen(
    vm: LoginViewModel = koinViewModel(),
    navigateTo: (String?) -> Unit
) {
    val state = vm.uiState.collectAsState()

    println(vm.device.toString())
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CustomButton(
            customButtonUi = CustomButtonUi(state.value.screenName, 300)
        ) {
            vm.handleEvent(Event.OnCustomButtonClicked)
        }
        CustomButton(
            customButtonUi = CustomButtonUi("Check token", 300)
        ) {
            vm.handleEvent(Event.OnCheckTokenButtonClicked)
        }
    }
}