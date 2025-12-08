package ru.bratusev.smartlab.feature_login


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ru.bratusev.smartlab.feature_login.components.InputFieldBlock
import ru.bratusev.smartlab.feature_login.models.Event
import ru.bratusev.smartlab.feature_login.models.LoginStage
import ru.bratusev.smartlab.ui.core.components.AnimatedLoadComponent
import smartlaboratory.ui.core.generated.resources.Res
import smartlaboratory.ui.core.generated.resources.auth_title
import smartlaboratory.ui.core.generated.resources.confirm
import smartlaboratory.ui.core.generated.resources.invalid_device_ip
import smartlaboratory.ui.core.generated.resources.network_error

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    vm: LoginViewModel = koinViewModel(),
    navigateToHome: () -> Unit,
) {
    val state = vm.uiState.collectAsState()
    var shouldOpenNetworkWarning by remember { mutableStateOf(!state.value.networkStatus.isIpInSameSubnet()) }

    LaunchedEffect(state.value.loginStage) {
        if (state.value.loginStage == LoginStage.COMPLETED_4) {
            navigateToHome()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(Res.string.auth_title),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            InputFieldBlock(
                screenState = state.value, isButtonEnabled = state.value.buttonEnabled,
                onLoginChanged = { login ->
                    vm.handleEvent(Event.OnLoginChanged(login))
                },
                onPasswordChanged = { password ->
                    vm.handleEvent(Event.OnPasswordChanged(password))
                },
                onLoginClicked = {
                    vm.handleEvent(Event.OnLoginClicked)
                },
            )

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                AnimatedVisibility(state.value.showError) {
                    Text(
                        stringResource(state.value.errorTextRes)
                    )
                }
                AnimatedLoadComponent(
                    Modifier.width(IntrinsicSize.Min).padding(horizontal = 24.dp, vertical = 16.dp),
                    state.value.animatedLoadUi.copy(isError = state.value.showError)
                )
            }
        }
        if (shouldOpenNetworkWarning) {
            AlertDialog(
                onDismissRequest = { shouldOpenNetworkWarning = false },
                title = { Text(stringResource(Res.string.network_error)) },
                text = { Text(stringResource(Res.string.invalid_device_ip)) },
                dismissButton = {
                    TextButton(
                        onClick = { shouldOpenNetworkWarning = false },
                        content = { Text(stringResource(Res.string.confirm)) }
                    )
                },
                confirmButton = {}
            )
        }
    }
}
