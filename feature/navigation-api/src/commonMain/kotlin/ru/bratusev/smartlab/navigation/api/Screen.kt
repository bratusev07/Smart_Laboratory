package ru.bratusev.smartlab.navigation.api

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object CustomScreen: Screen("customScreen") {
        object Main : Screen("mainCustomScreen")
        object AddWidget : Screen("addWidgetCustomScreen")
    }

    object Areas: Screen("areas")

    object Settings : Screen("settings")

    object Notifications : Screen("notifications")

    object UserProfile : Screen("UserProfile")
    object Logcat : Screen("logcat")
}
