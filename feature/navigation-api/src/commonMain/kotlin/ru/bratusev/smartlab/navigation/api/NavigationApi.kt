package ru.bratusev.smartlab.navigation.api

interface NavigationApi {
    fun navigateTo(screen: Screen)
    fun navigateToHome()
    fun navigateToLogin()
    fun navigateToSettings()
    fun navigateToLogcat()
    fun navigateToAreasScreen()
    fun navigateToDetailedArea(areaId: String, friendlyName: String?, pictureUrl: String?)

    fun navigateToAddWidgetCustomScreen()
    fun popBackStack()
}
