package ru.bratusev.smartlab.navigation

import androidx.navigation.NavController
import ru.bratusev.smartlab.navigation.api.NavigationApi
import ru.bratusev.smartlab.navigation.api.Screen

class NavigationApiImpl(
    private val navController: NavController
) : NavigationApi {
    
    override fun navigateTo(screen: Screen) {
        navController.navigate(screen.route)
    }
    
    override fun navigateToHome() {
        navController.navigate(Screen.Home.route)
    }
    
    override fun navigateToLogin() {
        navController.navigate(Screen.Login.route)
    }
    
    override fun navigateToSettings() {
        navController.navigate(Screen.Settings.route)
    }
    
    override fun navigateToLogcat() {
        navController.navigate(Screen.Logcat.route)
    }
    
    override fun popBackStack() {
        navController.popBackStack()
    }
}
