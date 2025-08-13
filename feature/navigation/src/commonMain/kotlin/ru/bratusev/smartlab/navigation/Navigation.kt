package ru.bratusev.smartlab.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.launch
import ru.bratusev.smartlab.feature_home.HomeScreen
import ru.bratusev.smartlab.feature_logcat.LogcatScreen
import ru.bratusev.smartlab.feature_login.LoginScreen
import ru.bratusev.smartlab.feature_settings.SettingsScreen
import ru.bratusev.smartlab.navigation.api.Screen
import ru.bratusev.smartlab.navigation.NavigationDrawer

@Composable
fun AppNavigation(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val drawerScope = rememberCoroutineScope()
    var isDrawerHidden by rememberSaveable { mutableStateOf(true) }
    val navigationApi = NavigationApiImpl(navController)

    NavigationDrawer(
        scope = drawerScope,
        drawerState = drawerState,
        isHidden = isDrawerHidden,
        navigate = { screen ->
            navigationApi.navigateTo(screen)
            drawerScope.launch {
                drawerState.close()
            }
        },
    ) { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            startDestination = Screen.Login.route
        ) {

            composable(Screen.Login.route) {
                isDrawerHidden = true
                LoginScreen(navigationApi = navigationApi)
            }

            composable(Screen.Home.route) {
                isDrawerHidden = false
                HomeScreen(navigationApi = navigationApi)
            }

            composable(Screen.Settings.route) {
                SettingsScreen(navigationApi = navigationApi)
            }

            composable(Screen.Logcat.route) {
                LogcatScreen(navigationApi = navigationApi)
            }
        }
    }
}