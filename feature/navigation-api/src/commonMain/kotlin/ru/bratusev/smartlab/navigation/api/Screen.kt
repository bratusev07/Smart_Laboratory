package ru.bratusev.smartlab.navigation.api

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Login : Screen()

    @Serializable
    data object Home : Screen()

    @Serializable
    sealed class CustomScreen : Screen() {
        @Serializable
        data object Root : CustomScreen()

        @Serializable
        data object Main : CustomScreen()

        @Serializable
        data object AddWidget : CustomScreen()
    }

    @Serializable
    sealed class Areas : Screen() {
        @Serializable
        data object Root : Areas()

        @Serializable
        data object All : Areas()

        @Serializable
        data class Detailed(val areaId: String, val friendlyName: String?, val pictureUrl: String?) : Areas()
    }

    @Serializable
    data object Settings : Screen()

    @Serializable
    data object Automation : Screen()

    @Serializable
    data object Notifications : Screen()

    @Serializable
    data object UserProfile : Screen()

    @Serializable
    data object Logcat : Screen()
}
