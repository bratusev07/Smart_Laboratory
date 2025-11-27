package ru.bratusev.smartlab.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import ru.bratusev.smartlab.feature_addWidgetScreen.AddWidgetScreen
import ru.bratusev.smartlab.feature_area.AreaScreen
import ru.bratusev.smartlab.feature_area.AreaScreenViewModel
import ru.bratusev.smartlab.feature_areas.AllAreasScreen
import ru.bratusev.smartlab.feature_areas.AllAreasScreenViewModel
import ru.bratusev.smartlab.feature_automation.AutomationScreen
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

    var topBarComposable by remember { mutableStateOf<@Composable RowScope.() -> Unit>({}) }
    val setTopBarComposable: (@Composable (RowScope.() -> Unit)) -> Unit = { newComposable ->
        topBarComposable = newComposable
    }

    val isDrawerHidden by remember(navBackStackEntry) {
        derivedStateOf {
            navBackStackEntry?.destination?.hierarchy?.any {
                it.hasRoute(Screen.Login::class) || it.hasRoute(
                    Screen.CustomScreen.AddWidget::class
                )
            } ?: false
        }
    }

    LaunchedEffect(isDrawerHidden) {
        if (!isDrawerHidden) {
            if (drawerState.currentValue == DrawerValue.Open) {
                drawerScope.launch { drawerState.close() }
            }
        }
    }
    NavigationDrawer(
        drawerScope = drawerScope,
        drawerState = drawerState,
        isHidden = isDrawerHidden,
        navigateTo = { screen ->
            drawerScope.launch { drawerState.close() }
            navigationApi.navigateTo(screen = screen)
        },
        navigationHierarchy = navBackStackEntry?.destination?.hierarchy,
        topContent = topBarComposable
    ) {
        AppNavHost(navController, navigationApi, setTopBarComposable)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun AppNavHost(
    navController: NavHostController,
    navigationApi: NavigationApi,
    setTopBarComposable: (@Composable (RowScope.() -> Unit)) -> Unit,
) {
    NavHost(
        navController = navController, startDestination = Screen.Login
    ) {
        composable<Screen.Login> {
            BackHandler(true) { /* Отключаем кнопку назад и т.п */ }
            LoginScreen(navigationApi = navigationApi)
        }

        composable<Screen.Home> {
            HomeScreen(navigationApi = navigationApi)
        }

        navigation<Screen.CustomScreen.Root>(
            startDestination = Screen.CustomScreen.Main
        ) {
            composable<Screen.CustomScreen.Main> {
                CustomScreen(
                    goToAddWidgetScreen = {
                        navigationApi.navigateToAddWidgetCustomScreen()
                    },
                    setTopBarComposable = setTopBarComposable,
                )
            }
            composable<Screen.CustomScreen.AddWidget> {
                AddWidgetScreen(
                    onGoBack = { navigationApi.popBackStack() })
            }
        }

        navigation<Screen.Areas.Root>(startDestination = Screen.Areas.All) {
            composable<Screen.Areas.All> {
                val allAreasScreenViewModel: AllAreasScreenViewModel = koinViewModel()
                val state = allAreasScreenViewModel.uiState.collectAsState()
                AllAreasScreen(
                    areas = state.value.areas,
                    navigateToArea = navigationApi::navigateToDetailedArea
                )
            }
            composable<Screen.Areas.Detailed> { backStackEntry ->
                val areaDetails = backStackEntry.toRoute() as Screen.Areas.Detailed
                val areaScreenViewModel: AreaScreenViewModel = koinViewModel()
                AreaScreen(
                    vm = areaScreenViewModel,
                    areaId = areaDetails.areaId,
                    friendlyName = areaDetails.friendlyName,
                    pictureUrl = areaDetails.pictureUrl
                )
            }
        }

        composable<Screen.Logcat> {
            LogcatScreen(navigationApi = navigationApi)
        }

        composable<Screen.Settings> {
            SettingsScreen(navigationApi = navigationApi)
        }

        composable<Screen.Automation> {
            AutomationScreen(navigationApi = navigationApi)
        }

        composable<Screen.Notifications> {
            Text(Screen.Notifications.toString())
        }

        composable<Screen.UserProfile> {
            Text(Screen.UserProfile.toString())
        }

    }
}