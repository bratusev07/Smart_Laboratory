package ru.bratusev.smartlab.navigation.api

interface NavigationApi {
    fun navigateTo(screen: Screen)
    fun navigateToHome()
    fun navigateToLogin()
    fun navigateToSettings()
    fun navigateToLogcat()

    fun navigateToAreasScreen()
    fun navigateToAddWidgetCustomScreen()
    fun popBackStack()
}
