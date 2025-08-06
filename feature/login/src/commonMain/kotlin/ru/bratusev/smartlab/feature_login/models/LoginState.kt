package ru.bratusev.smartlab.feature_login.models

data class LoginState(
    val screenName: String = "Login Screen",
    val login: String = "denis", //"admin",
    val password: String = "V+5G5]Aw2uK4HsW" //"tXRzB034gYgAH6"
)

sealed class Event {
    data object OnBackClicked: Event()
    data object OnCustomButtonClicked : Event()
    data object OnCheckTokenButtonClicked: Event()
}