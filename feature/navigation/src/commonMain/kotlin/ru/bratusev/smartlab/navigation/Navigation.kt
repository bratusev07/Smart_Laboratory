package ru.bratusev.smartlab.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AppNavigation(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val drawerScope = rememberCoroutineScope()
    var isDrawerHidden by rememberSaveable { mutableStateOf(true) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    fun navigateTo(screen: Screen) {
        isDrawerHidden = when (screen) {
            Screen.Login -> true
            else -> false
        }
        navController.navigate(screen.route)
    }

    NavigationDrawer(
        scope = drawerScope,
        drawerState = drawerState,
        isHidden = isDrawerHidden,
        navigateTo = {
            navigateTo(it)
        },
        selectedScreenRoute = navBackStackEntry?.destination?.route ?: "",
    ) {
        NavHost(
            navController = navController, startDestination = Screen.Login.route
        ) {

            composable(Screen.Login.route) {
                BackHandler(true) { /* Отключаем кнопку назад и т.п */ }
                LoginScreen(
                    navigateToHome = {
                        drawerScope.launch {
                            drawerState.close()
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


