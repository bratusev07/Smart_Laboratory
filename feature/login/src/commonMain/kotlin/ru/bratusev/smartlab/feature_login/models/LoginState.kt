package ru.bratusev.smartlab.feature_login.models

import org.jetbrains.compose.resources.StringResource
import ru.bratusev.smartlab.ui.core.models.AnimatedLoadUi

internal data class InternalLoginState(
    val login: String = "",
    val password: String = "",
    val loginStage: LoginStage = LoginStage.NOTHING_0,
    val networkStatus: NetworkStatusUi = NetworkStatusUi(
        ip = "", baseUrl = "", isVpnActive = false
    ),
    val addServerError: StringResource? = null
)

data class LoginState(
    val screenName: String = "Login Screen",
    val login: String,
    val password: String,
    val loginStage: LoginStage,
    val networkStatus: NetworkStatusUi,
    val servers: List<ServerInfo>,
    val currentServer: ServerInfo?,
    val addServerError: StringResource? = null
) {
    data class ServerInfo(
        val url: String,
        val name: String,
        val login: String,
        val password: String
    )

    internal constructor(internalLoginState: InternalLoginState) : this(
        login = internalLoginState.login,
        password = internalLoginState.password,
        loginStage = internalLoginState.loginStage,
        networkStatus = internalLoginState.networkStatus,
        servers = emptyList(),
        currentServer = null,
        addServerError = internalLoginState.addServerError
    )

    val buttonEnabled: Boolean
        get() = when (loginStage) {
            LoginStage.NOTHING_0 -> true
            LoginStage.FAILED_DURING_LOGIN -> true
            LoginStage.FAILED_DURING_TOKEN -> true
            else -> false
        }

    val showInputFields: Boolean
        get() = currentServer?.login.isNullOrEmpty()

    val showError: Boolean
        get() = when (loginStage) {
            LoginStage.FAILED_DURING_LOGIN -> true
            LoginStage.FAILED_DURING_TOKEN -> true
            else -> false
        }
    val loginErrorTextRes: StringResource
        get() = loginStage.stateStringRes

    val animatedLoadUi: AnimatedLoadUi
        get() = AnimatedLoadUi(loginStage.stateStringRes, loginStage.isShowing, false)
}

sealed class Event {
    data object OnBackClicked : Event()
    data object OnLoginClicked : Event()
    data class OnLoginChanged(val value: String) : Event()
    data class OnPasswordChanged(val value: String) : Event()
    data class OnCurrentServerChanged(val newUrl: String?, val newName: String?) : Event()
    data class OnServerDeleted(val deletedUrl: String, val deletedName: String) : Event()
    data class OnServerAdded(
        val newUrl: String,
        val newName: String,
        val newLogin: String,
        val newPassword: String
    ) : Event()

    data object ClearAddServerError : Event()
}