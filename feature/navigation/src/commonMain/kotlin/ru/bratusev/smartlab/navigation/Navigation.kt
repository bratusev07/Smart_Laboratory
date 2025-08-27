package ru.bratusev.smartlab.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navigation
import ru.bratusev.smartlab.feature_addWidgetScreen.AddWidgetScreen
import ru.bratusev.smartlab.feature_customScreen.CustomScreen
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

    var onMenuClickAction by remember { mutableStateOf({}) }
    val setMenuAction: (() -> Unit) -> Unit = { newAction ->
        onMenuClickAction = newAction
    }

    val isDrawerHidden by remember {
        derivedStateOf {
            when (navBackStackEntry?.destination?.route) {
                Screen.Login.route -> true
                Screen.CustomScreen.AddWidget.route -> true
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
        onMenuClick = onMenuClickAction,
    ) {
        AppNavHost(navController, navigationApi, setMenuAction)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun AppNavHost(
    navController: NavHostController,
    navigationApi: NavigationApi,
    setMenuAction: (action: () -> Unit) -> Unit,
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


        navigation(
            startDestination = Screen.CustomScreen.Main.route,
            route = Screen.CustomScreen.route
        ) {
            composable(Screen.CustomScreen.Main.route) {
                CustomScreen(
                    setMenuAction = setMenuAction,
                    goToAddWidgetScreen = {
                        navigationApi.navigateToAddWidgetCustomScreen()
                    }
                )
            }
            composable(
                Screen.CustomScreen.AddWidget.route
            ) {
                AddWidgetScreen()
            }
        }

        composable(Screen.Logcat.route) {
            LogcatScreen(navigationApi = navigationApi)
        }

        composable(Screen.Settings.route) {
            SettingsScreen(navigationApi = navigationApi)
        }


        composable(Screen.Notifications.route) {
            Text(Screen.Notifications.route)
        }

        composable(Screen.UserProfile.route) {
            Text(Screen.UserProfile.route)
        }

    }
}