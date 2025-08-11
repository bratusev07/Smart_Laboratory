package ru.bratusev.smartlab.feature_login.models

import ru.bratusev.smartlab.ui.core.models.AnimatedLoadUi

data class LoginState(
    val screenName: String = "Login Screen",
    val login: String = "admin",
    val password: String = "tXRzB034gYgAH6",
    val loginStage: LoginStage = LoginStage.NOTHING_0,
) {
    val animatedLoadUi: AnimatedLoadUi
        get() = AnimatedLoadUi(loginStage.stateText, loginStage.isShowing)
}

sealed class Event {
    data object OnBackClicked : Event()
    data object OnLoginClicked : Event()
    data class OnLoginChanged(val value: String) : Event()
    data class OnPasswordChanged(val value: String) : Event()
}