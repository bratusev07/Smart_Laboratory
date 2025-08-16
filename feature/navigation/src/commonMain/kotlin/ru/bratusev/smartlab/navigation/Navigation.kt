package ru.bratusev.smartlab.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.bratusev.smartlab.feature_home.HomeScreen
import ru.bratusev.smartlab.feature_logcat.LogcatScreen
import ru.bratusev.smartlab.feature_login.LoginScreen
import ru.bratusev.smartlab.feature_settings.SettingsScreen
import ru.bratusev.smartlab.navigation.api.NavigationApi
import ru.bratusev.smartlab.navigation.api.Screen

@Composable
fun AppNavigation(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val drawerScope = rememberCoroutineScope()
    val navigationApi = NavigationApiImpl(navController)
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val isDrawerHidden by remember {
        derivedStateOf {
            when (navBackStackEntry?.destination?.route) {
                Screen.Login.route -> true
                else -> false
            }
        }
    }
    NavigationDrawer(
        drawerScope = drawerScope,
        drawerState = drawerState,
        isHidden = isDrawerHidden,
        navigateTo = { screen ->
            navigationApi.navigateTo(screen = screen)
        },
        currentScreenRoute = navBackStackEntry?.destination?.route ?: "",
    ) {
        AppNavHost(navController, navigationApi)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun AppNavHost(
    navController: NavHostController,
    navigationApi: NavigationApi,
) {
    NavHost(
        navController = navController, startDestination = Screen.Login.route
    ) {

        composable(Screen.Login.route) {
            BackHandler(true) { /* Отключаем кнопку назад и т.п */ }
            LoginScreen(
                navigateToHome = { navigationApi.navigateToHome() })
        }

        composable(Screen.Home.route) {
            HomeScreen(navigationApi = navigationApi)
        }

        composable(Screen.Settings.route) {
            SettingsScreen(navigationApi = navigationApi)
        }

        composable(Screen.Logcat.route) {
            LogcatScreen(navigationApi = navigationApi)
        }

        composable(Screen.Notifications.route) {
            Text(Screen.Notifications.route)
        }
        composable(Screen.UserProfile.route) {
            Text(Screen.UserProfile.route)
        }
    }
}