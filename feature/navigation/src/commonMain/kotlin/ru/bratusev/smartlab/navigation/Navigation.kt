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
import ru.bratusev.smartlab.feature_home.NavigationDrawer
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

    NavigationDrawer(
        scope = drawerScope,
        drawerState = drawerState,
        isHidden = isDrawerHidden,
        navigateToAuthScreen = {
            navController.navigate(Screen.Login.route)
            isDrawerHidden = true
        }) { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            startDestination = Screen.Login.route
        ) {

            composable(Screen.Login.route) {
                LoginScreen(
                    navigateToHome = {
                        drawerScope.launch {
                            drawerState.close()
                            isDrawerHidden = false
                            navController.navigate(Screen.Home.route)
                        }
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
            }
        }
    }
}

fun mutableStateOf(bool: Boolean) {}

private fun NavController.navigate(route: String?) {
    if (route == null) {
        // TODO: пока уберу. Вызывает баг, если много раз нажать на переход
        //  this.popBackStack()
    } else {
        this.navigate(route)
    }
}