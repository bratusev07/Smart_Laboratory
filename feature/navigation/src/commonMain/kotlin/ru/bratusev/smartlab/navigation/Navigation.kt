package ru.bratusev.smartlab.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.bratusev.smartlab.feature_home.HomeScreen
import ru.bratusev.smartlab.feature_login.LoginScreen
import ru.bratusev.smartlab.feature_settings.SettingsScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Settings : Screen("settings")
}

@Composable
fun AppNavigation(navController: NavHostController) {
    //AppTheme {
        NavHost(
            navController = navController, startDestination = Screen.Login.route
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    navigateToHome = {
                        navController.navigate(Screen.Home.route)
                    })
            }

            composable(Screen.Home.route) {
                HomeScreen(
                    navigateTo = {
                        navController.navigate(it)
                    })
            }

            composable(Screen.Settings.route) {
                SettingsScreen(
                    navigateTo = {
                        navController.navigate(it)
                    })
        //    }
        }
    }
}

private fun NavController.navigate(route: String?) {
    if (route == null) {
        this.popBackStack()
    } else {
        this.navigate(route)
    }
}