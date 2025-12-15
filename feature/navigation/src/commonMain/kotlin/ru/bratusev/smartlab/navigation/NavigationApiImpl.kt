package ru.bratusev.smartlab.navigation

import androidx.navigation.NavController
import ru.bratusev.smartlab.navigation.api.NavigationApi
import ru.bratusev.smartlab.navigation.api.Screen

class NavigationApiImpl(
    private val navController: NavController,
) : NavigationApi {

    override fun navigateTo(screen: Screen) {
        navController.navigate(screen)
    }

    override fun navigateToHome() {
        navController.navigate(Screen.Home) {
            // очищаем стэк, чтобы нельзя было вернуться к авторизации просто нажав назад
            popUpTo<Screen.Login> {
                inclusive = true
            }
        }
    }

    override fun navigateToLogin() {
        navController.navigate(Screen.Login)
    }

    override fun navigateToSettings() {
        navController.navigate(Screen.Settings)
    }

    override fun navigateToLogcat() {
        navController.navigate(Screen.Logcat)
    }

    override fun navigateToAreasScreen() {
        navController.navigate(Screen.Areas)
    }

    override fun navigateToAutomationScreen() {
        navController.navigate(Screen.Automation)
    }

    override fun navigateToDetailedArea(
        areaId: String,
        friendlyName: String?,
        pictureUrl: String?
    ) {
        navController.navigate(Screen.Areas.Detailed(areaId, friendlyName, pictureUrl)){
            popUpTo<Screen.Areas.Detailed> {
                inclusive = true
            }
        }
    }

    override fun navigateToAddWidgetCustomScreen() {
        navController.navigate(Screen.CustomScreen.AddWidget)
    }

    override fun popBackStack() {
        navController.popBackStack()
    }
}
