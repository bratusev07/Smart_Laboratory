package ru.bratusev.smartlab.feature_login


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import ru.bratusev.smartlab.feature_login.models.Event
import ru.bratusev.smartlab.ui.core.components.tileButton.TileButton
import ru.bratusev.smartlab.ui.core.models.tileButton.TileButtonUi

@Composable
fun LoginScreen(
    vm: LoginViewModel = koinViewModel(), navigateTo: (String?) -> Unit,
) {
    val state = vm.uiState.collectAsState()

    println(vm.device.toString())

    var isLightOn by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TileButton(
            modifier = Modifier.size(150.dp), tileButtonUi = TileButtonUi.LightBulb(
                location = state.value.screenName,
                isOn = isLightOn,
                isEnabled = false
            )
        ) {
            isLightOn = !isLightOn
            vm.handleEvent(Event.OnCustomButtonClicked)
        }
        TileButton(
            modifier = Modifier.size(168.dp), tileButtonUi = TileButtonUi.Thermometer(
                location = "Check token", temperature = 127.3f, isEnabled = true
            )
        ) {
            vm.handleEvent(Event.OnCheckTokenButtonClicked)
        }
    }
}