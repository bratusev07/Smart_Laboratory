package ru.bratusev.smartlab.navigation.api

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Settings : Screen("settings")
    object Logcat : Screen("logcat")
}
