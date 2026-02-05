package ru.bratusev.smartlab.feature_login.models

import org.jetbrains.compose.resources.StringResource
import ru.bratusev.smartlab.ui.core.models.AnimatedLoadUi

internal data class InternalLoginState(
    val login: String = "denis",
    val password: String = "V+5G5]Aw2uK4HsW",
    val loginStage: LoginStage = LoginStage.NOTHING_0,
    val networkStatus: NetworkStatusUi = NetworkStatusUi(
        ip = "", baseUrl = "", isVpnActive = false
    ),
)

data class LoginState(
    val screenName: String = "Login Screen",
    val login: String,
    val password: String,
    val loginStage: LoginStage,
    val networkStatus: NetworkStatusUi,
    val servers: Map<String, String>,
    val currentServerUrl: String?,
) {

    internal constructor(internalLoginState: InternalLoginState) : this(
        login = internalLoginState.login,
        password = internalLoginState.password,
        loginStage = internalLoginState.loginStage,
        networkStatus = internalLoginState.networkStatus,
        servers = emptyMap(),
        currentServerUrl = null
    )

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
    data class OnCurrentServerChanged(val newUrl: String?) : Event()
    data class OnServerDeleted(val deletedUrl: String) : Event()
    data class OnServerAdded(val newUrl: String, val newName: String) : Event()
}