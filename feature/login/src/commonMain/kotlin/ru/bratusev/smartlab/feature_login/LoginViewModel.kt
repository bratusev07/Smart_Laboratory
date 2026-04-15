package ru.bratusev.smartlab.feature_login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
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
import smartlaboratory.ui.core.generated.resources.Res
import smartlaboratory.ui.core.generated.resources.error_server_exists
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

        val uiServers = serverSelection.servers.map { list ->
            LoginState.ServerInfo(
                url = list.getOrElse(0) { "" },
                name = list.getOrElse(1) { "" },
                login = list.getOrElse(2) { "" },
                password = list.getOrElse(3) { "" }
            )
        }

        val selectedServer = uiServers.find {
            it.url == serverSelection.currentServerUrl && it.name == serverSelection.currentServerName
        }

        LoginState(
            login = internalState.login,
            password = internalState.password,
            loginStage = internalState.loginStage,
            networkStatus = internalState.networkStatus,
            servers = uiServers,
            currentServer = selectedServer,
            addServerError = internalState.addServerError
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LoginState(InternalLoginState())
    )

    init {
        val networkStatus = getNetworkStatusUseCases().toUi()
        updateState { it.copy(networkStatus = networkStatus) }

        getServerSelectionFlowUseCase()
            .distinctUntilChanged { old, new ->
                old.currentServerUrl == new.currentServerUrl && old.currentServerName == new.currentServerName
            }
            .onEach { selection ->
                val current = selection.servers.find {
                    it.getOrElse(0) { "" } == selection.currentServerUrl &&
                            it.getOrElse(1) { "" } == selection.currentServerName
                }

                if (current != null) {
                    updateState { it.copy(
                        login = current.getOrElse(2) { "" },
                        password = current.getOrElse(3) { "" }
                    ) }
                }
            }
            .launchIn(viewModelScope)
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
        loginUseCase.invoke(
            uiState.value.login, uiState.value.password, device
        ).onEach { result ->
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

    private fun onCurrentServerChanged(newUrl: String?, newName: String?) {
        val selectedServer = uiState.value.servers.find {
            it.url == newUrl && it.name == newName
        }

        updateState {
            it.copy(
                login = selectedServer?.login ?: "",
                password = selectedServer?.password ?: ""
            )
        }

        changeServerSelection(currentServerUrl = newUrl, currentServerName = newName)
    }

    private fun onServerDeleted(urlToDelete: String, nameToDelete: String) {
        val newServerList = uiState.value.servers.filter {
            !(it.url == urlToDelete && it.name == nameToDelete)
        }

        val current = uiState.value.currentServer
        val newCurrentUrl = if (current?.url == urlToDelete && current.name == nameToDelete) null
        else current?.url

        changeServerSelection(servers = newServerList, currentServerUrl = newCurrentUrl)
    }

    private fun onServerAdded(
        newUrl: String,
        newName: String,
        newLogin: String,
        newPassword: String
    ) {
        val existingServers = uiState.value.servers

        val isDuplicate = existingServers.any { it.url == newUrl && it.name == newName }

        if (isDuplicate) {
            updateState { it.copy(addServerError = Res.string.error_server_exists) }
        } else {
            val newServer = LoginState.ServerInfo(
                url = newUrl,
                name = newName,
                login = newLogin,
                password = newPassword
            )
            val newServerList = existingServers + newServer
            changeServerSelection(servers = newServerList)
            updateState { it.copy(addServerError = null) }
        }
    }

    private fun changeServerSelection(
        servers: List<LoginState.ServerInfo> = uiState.value.servers,
        currentServerUrl: String? = uiState.value.currentServer?.url,
        currentServerName: String? = uiState.value.currentServer?.name
    ) {
        viewModelScope.launch {
            val domainServers = servers.map { info ->
                listOf(info.url, info.name, info.login, info.password)
            }

            setServerSelectionUseCase(
                serverSelection = ServerSelection(
                    servers = domainServers,
                    currentServerUrl = currentServerUrl,
                    currentServerName = currentServerName
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
            is Event.OnCurrentServerChanged -> onCurrentServerChanged(event.newUrl, event.newName)
            is Event.OnServerDeleted -> onServerDeleted(event.deletedUrl, event.deletedName)
            is Event.OnServerAdded -> onServerAdded(
                event.newUrl,
                event.newName,
                event.newLogin,
                event.newPassword
            )

            Event.ClearAddServerError -> updateState { it.copy(addServerError = null) }
        }
    }
}