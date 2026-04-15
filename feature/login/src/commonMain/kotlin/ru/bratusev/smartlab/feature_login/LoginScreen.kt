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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import ru.bratusev.smartlab.navigation.api.NavigationApi
import ru.bratusev.smartlab.ui.core.components.AnimatedLoadComponent
import ru.bratusev.smartlab.ui.core.components.ServerSelectionDropDown
import ru.bratusev.smartlab.ui.core.models.ServerSelectionUi
import smartlaboratory.ui.core.generated.resources.Res
import smartlaboratory.ui.core.generated.resources.auth_title
import smartlaboratory.ui.core.generated.resources.confirm
import smartlaboratory.ui.core.generated.resources.invalid_device_ip
import smartlaboratory.ui.core.generated.resources.network_error

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    vm: LoginViewModel = koinViewModel(),
    navigationApi: NavigationApi,
) {
    val state by vm.uiState.collectAsState()
    var shouldOpenNetworkWarning by remember { mutableStateOf(!state.networkStatus.isIpInSameSubnet()) }

    val snackbarHostState = remember { SnackbarHostState() }
    val addServerError = state.addServerError

    LaunchedEffect(state.loginStage) {
        if (state.loginStage == LoginStage.COMPLETED_4) {
            navigationApi.navigateToHome()
        }
    }

    if (addServerError != null) {
        val errorMessage = stringResource(addServerError)
        LaunchedEffect(addServerError) {
            snackbarHostState.showSnackbar(errorMessage)
            vm.handleEvent(Event.ClearAddServerError)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
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
                var expanded by remember { mutableStateOf(false) }

                ServerSelectionDropDown(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    uiData = ServerSelectionUi(
                        serverList = state.servers.map { server ->
                            ServerSelectionUi.ServerInfoUi(
                                url = server.url,
                                name = server.name,
                                login = server.login,
                                password = server.password
                            )
                        },
                        currentServer = state.currentServer?.let {
                            ServerSelectionUi.ServerInfoUi(it.url, it.name, it.login, it.password)
                        },
                        expanded = expanded
                    ),
                    onSelect = { serverInfo ->
                        vm.handleEvent(
                            Event.OnCurrentServerChanged(
                                serverInfo.url,
                                serverInfo.name
                            )
                        )
                        expanded = false
                    },
                    onExpand = { expanded = true },
                    onClose = { expanded = false },
                    onDelete = { serverInfo ->
                        vm.handleEvent(Event.OnServerDeleted(serverInfo.url, serverInfo.name))
                    },
                    onAddServer = { url, name, login, password ->
                        vm.handleEvent(Event.OnServerAdded(url, name, login, password))
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))

                InputFieldBlock(
                    screenState = state,
                    isButtonEnabled = state.buttonEnabled,
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
                    AnimatedVisibility(state.showError) {
                        Text(
                            stringResource(state.loginErrorTextRes),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    AnimatedLoadComponent(
                        Modifier.width(IntrinsicSize.Min)
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        state.animatedLoadUi.copy(isError = state.showError)
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
                            content = { Text(stringResource(Res.string.confirm)) })
                    },
                    confirmButton = {})
            }
        }
    }
}