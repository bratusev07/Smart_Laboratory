package ru.bratusev.smartlab.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.launch
import ru.bratusev.smartlab.feature_home.HomeScreen
import ru.bratusev.smartlab.feature_login.LoginScreen
import ru.bratusev.smartlab.feature_settings.SettingsScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Settings : Screen("settings")
}

// TODO: кол-во фич = кол-во багов
@Composable
fun AppNavigation(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val drawerScope = rememberCoroutineScope()
    var isDrawerHidden by rememberSaveable { mutableStateOf(true) }
    var currentScreenRoute: String by rememberSaveable { mutableStateOf(Screen.Home.route) }

    fun navigateTo(screen: Screen) {
        currentScreenRoute = screen.route
        navController.navigate(screen.route)
    }

    NavigationDrawer(
        scope = drawerScope,
        drawerState = drawerState,
        isHidden = isDrawerHidden,
        navigateTo = {
            navigateTo(it)
        },
        selectedScreenRoute = currentScreenRoute,
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route
        ) {

            composable(Screen.Login.route) {
                LoginScreen(
                    navigateToHome = {
                        drawerScope.launch {
                            drawerState.close()
                            isDrawerHidden = false
                            navigateTo(Screen.Home)
                        }
                    })
            }

            composable(Screen.Home.route) {
                HomeScreen()
            }

            composable(Screen.Settings.route) {
                SettingsScreen()
            }
        }
    }
}


