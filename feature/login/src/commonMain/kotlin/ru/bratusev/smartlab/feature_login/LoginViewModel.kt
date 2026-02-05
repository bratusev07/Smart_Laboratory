package ru.bratusev.smartlab.feature_login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.bratusev.smartlab.domain.core.model.ServerSelection
import ru.bratusev.smartlab.domain.core.usecase.GetLoggerUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetLoginUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetNetworkStatusUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetServerSelectionFlowUseCase
import ru.bratusev.smartlab.domain.core.usecase.SetServerSelectionUseCase
import ru.bratusev.smartlab.feature_login.mappers.toUi
import ru.bratusev.smartlab.feature_login.models.Device
import ru.bratusev.smartlab.feature_login.models.Event
import ru.bratusev.smartlab.feature_login.models.InternalLoginState
import ru.bratusev.smartlab.feature_login.models.LoginStage
import ru.bratusev.smartlab.feature_login.models.LoginState
import ru.bratusev.smartlab.domain.core.model.Device as DomainDevice

class LoginViewModel(
    private val loginUseCase: GetLoginUseCase,
    private val logger: GetLoggerUseCase,
    getNetworkStatusUseCases: GetNetworkStatusUseCase,
    getServerSelectionFlowUseCase: GetServerSelectionFlowUseCase,
    private val setServerSelectionUseCase: SetServerSelectionUseCase,
    val device: Device,
) : ViewModel() {

    private val _internalUiState = MutableStateFlow(InternalLoginState())
    val uiState: StateFlow<LoginState> = combine(
        getServerSelectionFlowUseCase(),
        _internalUiState
    ) { serverSelection, internalState ->
        LoginState(
            login = internalState.login,
            password = internalState.password,
            loginStage = internalState.loginStage,
            networkStatus = internalState.networkStatus,
            servers = serverSelection.servers,
            currentServerUrl = serverSelection.currentServerUrl,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LoginState(InternalLoginState())
    )

    init {
        val networkStatus = getNetworkStatusUseCases().toUi()
        updateState { it.copy(networkStatus = networkStatus) }
    }

    private fun onLoginChanged(newLogin: String) {
        updateState { it.copy(login = newLogin) }
    }

    private fun onPasswordChanged(newPassword: String) {
        updateState { it.copy(password = newPassword) }
    }

    private fun onLoginClicked() {
        val device = DomainDevice(
            appId = device.appId,
            appName = device.appName,
            appVersion = device.appVersion,
            deviceName = device.deviceName,
            manufacturer = device.manufacturer,
            model = device.model,
            osName = device.osName,
            osVersion = device.osVersion,
            deviceId = device.deviceId
        )

        updateState { it.copy(loginStage = LoginStage.START_1) }
        loginUseCase.invoke(uiState.value.login, uiState.value.password, device).onEach { result ->
            result.fold(onSuccess = { token ->
                // TODO Исправить показуху на нормальное отслеживание
                delay(1200)
                updateState { it.copy(loginStage = LoginStage.SAVING_TOKEN_2) }
                delay(1200)
                updateState { it.copy(loginStage = LoginStage.CHECKING_TOKEN_3) }
                delay(1200)
                updateState { it.copy(loginStage = LoginStage.COMPLETED_4) }
                logger.d("viewModel", "loginUseCase returned success with token: $token")
            }, onFailure = { error ->
                delay(400)
                updateState { it.copy(loginStage = LoginStage.FAILED_DURING_LOGIN) }
                logger.d("viewModel", "loginUseCase returned failure. Error is $error")
            })
        }.launchIn(viewModelScope)
    }

    private fun onCurrentServerChanged(newCurrentUrl: String?) {
        changeServerSelection(currentServerUrl = newCurrentUrl)
    }

    private fun onServerDeleted(urlToDelete: String) {
        val newServerList = uiState.value.servers.filter { it.key != urlToDelete }
        val newCurrentUrl =
            if (uiState.value.currentServerUrl == urlToDelete) null else uiState.value.currentServerUrl
        changeServerSelection(servers = newServerList, currentServerUrl = newCurrentUrl)
    }

    private fun onServerAdded(newUrl: String, newName: String) {
        if (newUrl !in uiState.value.servers.keys && newName !in uiState.value.servers.values) {
            val newServerList = uiState.value.servers + (newUrl to newName)
            changeServerSelection(servers = newServerList)
        }
    }

    private fun changeServerSelection(
        servers: Map<String, String> = uiState.value.servers,
        currentServerUrl: String? = uiState.value.currentServerUrl
    ) {
        viewModelScope.launch {
            setServerSelectionUseCase(
                serverSelection = ServerSelection(
                    servers = servers, currentServerUrl = currentServerUrl
                )
            )
        }
    }

    private fun updateState(mutation: (InternalLoginState) -> InternalLoginState) {
        _internalUiState.update(mutation)
    }

    internal fun handleEvent(event: Event) {
        when (event) {
            Event.OnBackClicked -> Unit
            Event.OnLoginClicked -> onLoginClicked()
            is Event.OnLoginChanged -> onLoginChanged(event.value)
            is Event.OnPasswordChanged -> onPasswordChanged(event.value)
            is Event.OnCurrentServerChanged -> onCurrentServerChanged(event.newUrl)
            is Event.OnServerDeleted -> onServerDeleted(event.deletedUrl)
            is Event.OnServerAdded -> onServerAdded(event.newUrl, event.newName)
        }
    }
}