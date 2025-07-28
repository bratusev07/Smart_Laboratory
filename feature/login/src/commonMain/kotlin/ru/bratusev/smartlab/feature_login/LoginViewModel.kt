package ru.bratusev.smartlab.feature_login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.bratusev.smartlab.domain.core.usecase.GetTokenUseCase
import ru.bratusev.smartlab.domain.core.model.Device as DomainDevice
import ru.bratusev.smartlab.domain.core.usecase.LoginUseCase
import ru.bratusev.smartlab.domain.core.usecase.SaveTokenUseCase
import ru.bratusev.smartlab.feature_login.models.Device
import ru.bratusev.smartlab.feature_login.models.Event
import ru.bratusev.smartlab.feature_login.models.LoginState

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val saveTokenUseCase: SaveTokenUseCase,
    private val getTokenUseCase: GetTokenUseCase,
    val device: Device
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState

    private fun onCustomButtonClicked() {
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
        // TODO: выглядит ну прям так себе
        loginUseCase.invoke(uiState.value.login, uiState.value.password, device).onEach { result ->
            result.fold(onSuccess = { token ->
                println("loginUseCase returned success with token: $token")
                saveTokenUseCase.invoke(token).onEach { result ->
                    result.fold(onSuccess = {
                        println("Token saved checking it's existence")
                        getTokenUseCase.invoke().onEach { result ->
                            result.fold(onSuccess = { token ->
                                println("Got token after saving: $token")
                            }, onFailure = { error ->
                                println("Error: token was saved, but couldn't be gotten. Error message is: $error")
                            })
                        }.launchIn(viewModelScope)
                    }, onFailure = {
                        println("Error during token saving")
                    })
                }.launchIn(viewModelScope)
            }, onFailure = { error ->
                println("loginUseCase returned failure. Error is $error")
            })
        }.launchIn(viewModelScope)
    }

    private fun checkToken() {
        getTokenUseCase.invoke().onEach { result ->
            result.fold(
                onSuccess = { token -> println("Checked token. It is $token") },
                onFailure = { error -> println("Error during checking token. Error is $error") })
        }.launchIn(viewModelScope)
    }

    private fun updateState(updatedState: LoginState) {
        viewModelScope.launch {
            _uiState.emit(updatedState)
        }
    }

    internal fun handleEvent(event: Event) {
        when (event) {
            Event.OnBackClicked -> Unit
            Event.OnCustomButtonClicked -> onCustomButtonClicked()
            Event.OnCheckTokenButtonClicked -> checkToken()
        }
    }
}