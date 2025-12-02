package ru.bratusev.smartlab.feature_login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.bratusev.smartlab.domain.core.usecase.GetLoggerUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetLoginUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetVpnStatusUseCase
import ru.bratusev.smartlab.feature_login.models.Device
import ru.bratusev.smartlab.feature_login.models.Event
import ru.bratusev.smartlab.feature_login.models.LoginStage
import ru.bratusev.smartlab.feature_login.models.LoginState
import ru.bratusev.smartlab.domain.core.model.Device as DomainDevice

class LoginViewModel(
    private val loginUseCase: GetLoginUseCase,
    private val logger: GetLoggerUseCase,
    private val vpnStatusUseCase: GetVpnStatusUseCase,
    val device: Device,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState

    init {
        val isUsingVpn = vpnStatusUseCase()
        updateState(_uiState.value.copy(isUsingVpn = isUsingVpn))
    }

    private fun onLoginChanged(newLogin: String) {
        updateState(_uiState.value.copy(login = newLogin))
    }

    private fun onPasswordChanged(newPassword: String) {
        updateState(_uiState.value.copy(password = newPassword))
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

        updateState(_uiState.value.copy(loginStage = LoginStage.START_1))

        loginUseCase.invoke(uiState.value.login, uiState.value.password, device).onEach { result ->
            result.fold(onSuccess = { token ->
                // TODO Исправить показуху на нормальное отслеживание
                delay(1200)
                updateState(_uiState.value.copy(loginStage = LoginStage.SAVING_TOKEN_2))
                delay(1200)
                updateState(_uiState.value.copy(loginStage = LoginStage.CHECKING_TOKEN_3))
                delay(1200)
                updateState(_uiState.value.copy(loginStage = LoginStage.COMPLETED_4))
                logger.d("viewModel", "loginUseCase returned success with token: $token")
            }, onFailure = { error ->
                delay(400)
                updateState(_uiState.value.copy(loginStage = LoginStage.FAILED_DURING_LOGIN))
                logger.d("viewModel", "loginUseCase returned failure. Error is $error")
            })
        }.launchIn(viewModelScope)
    }

    private fun updateState(updatedState: LoginState) {
        viewModelScope.launch {
            logger.d(
                "viewModel",
                "Updating state: loginStage=${updatedState.loginStage}, animatedLoadUi=${updatedState.animatedLoadUi}"
            )
            _uiState.emit(updatedState)
        }
    }

    internal fun handleEvent(event: Event) {
        when (event) {
            Event.OnBackClicked -> Unit
            Event.OnLoginClicked -> onLoginClicked()
            is Event.OnLoginChanged -> onLoginChanged(event.value)
            is Event.OnPasswordChanged -> onPasswordChanged(event.value)
        }
    }
}