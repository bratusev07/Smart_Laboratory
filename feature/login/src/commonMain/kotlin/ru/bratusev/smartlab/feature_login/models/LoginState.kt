package ru.bratusev.smartlab.feature_login.models

import org.jetbrains.compose.resources.StringResource
import ru.bratusev.smartlab.ui.core.models.AnimatedLoadUi

data class LoginState(
    val screenName: String = "Login Screen",
    val login: String = "denis",
    val password: String = "V+5G5]Aw2uK4HsW",
    val loginStage: LoginStage = LoginStage.NOTHING_0,
    val networkStatus: NetworkStatusUi = NetworkStatusUi(
        ip = "",
        baseUrl = "",
        isVpnActive = false
    )
) {
    val buttonEnabled: Boolean
        get() = when (loginStage) {
            LoginStage.NOTHING_0 -> true
            LoginStage.FAILED_DURING_LOGIN -> true
            LoginStage.FAILED_DURING_TOKEN -> true
            else -> false
        }

    val showError: Boolean
        get() = when (loginStage) {
            LoginStage.FAILED_DURING_LOGIN -> true
            LoginStage.FAILED_DURING_TOKEN -> true
            else -> false
        }
    val errorTextRes: StringResource
        get() = loginStage.stateStringRes

    val animatedLoadUi: AnimatedLoadUi
        get() = AnimatedLoadUi(loginStage.stateStringRes, loginStage.isShowing, false)
}

sealed class Event {
    data object OnBackClicked : Event()
    data object OnLoginClicked : Event()
    data class OnLoginChanged(val value: String) : Event()
    data class OnPasswordChanged(val value: String) : Event()
}