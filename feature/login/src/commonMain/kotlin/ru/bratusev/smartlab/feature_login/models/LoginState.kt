package ru.bratusev.smartlab.feature_login.models

import ru.bratusev.smartlab.feature_login.LoginStage

data class LoginState(
    val screenName: String = "Login Screen",
    val login: String = "admin",
    val password: String = "tXRzB034gYgAH6",
    val loginStage: LoginStage = LoginStage.START_1
)

sealed class Event {
    data object OnBackClicked: Event()
    data object OnCustomButtonClicked : Event()
    data object OnCheckTokenButtonClicked: Event()
}