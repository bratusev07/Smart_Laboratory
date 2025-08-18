package ru.bratusev.smartlab.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.navigation.api.Screen
import ru.bratusev.smartlab.navigation.models.NavigationDrawerItems
import ru.bratusev.smartlab.ui.core.components.AppTopBar
import ru.bratusev.smartlab.ui.core.models.AppTopBarUi
import ru.bratusev.smartlab.ui.core.resources.StringsRes
import ru.bratusev.smartlab.ui.core.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer(
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    drawerScope: CoroutineScope = rememberCoroutineScope(),
    isHidden: Boolean = false,
    navigateTo: (Screen) -> Unit,
    currentScreenRoute: String,
    content: @Composable () -> Unit,
) {
    ModalNavigationDrawer(
        modifier = Modifier.wrapContentWidth(), gesturesEnabled = !isHidden, drawerContent = {
            if (!isHidden) {
                ModalDrawerSheet {
                    Column(
                        modifier = Modifier.fillMaxHeight().verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(top = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            IconButton(onClick = {
                                drawerScope.launch {
                                    drawerState.close()
                                }
                            }) {
                                Icon(Icons.Default.Close, contentDescription = null)
                            }
                            Text(
                                StringsRes.DRAWER_TITLE,
                                modifier = Modifier.padding(vertical = 16.dp),
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        HorizontalDivider()
                        NavigationDrawerItemComponent(
                            NavigationDrawerItems.Home,
                            currentScreenRoute = currentScreenRoute,
                            navigateTo = navigateTo
                        )
                        NavigationDrawerItemComponent(
                            NavigationDrawerItems.Logs,
                            currentScreenRoute = currentScreenRoute,
                            navigateTo = navigateTo
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        NavigationDrawerItemComponent(
                            NavigationDrawerItems.Settings,
                            currentScreenRoute = currentScreenRoute,
                            navigateTo = navigateTo
                        )
                        HorizontalDivider()
                        NavigationDrawerItemComponent(
                            NavigationDrawerItems.Notifications,
                            currentScreenRoute = currentScreenRoute,
                            navigateTo = navigateTo
                        )
                        NavigationDrawerItemComponent(
                            NavigationDrawerItems.Profile,
                            currentScreenRoute = currentScreenRoute,
                            navigateTo = navigateTo
                        )
                    }
                }
            }
        }, drawerState = drawerState
    ) {
        if (isHidden) {
            content()
        } else {
            Scaffold(
                topBar = {
                    AppTopBar(
                        uiData = AppTopBarUi(
                            // TODO: Убрать захардкоженное значение
                            title = "Лаборатория 1",
                        ),
                        onTitleClick = {
                            drawerScope.launch {
                                drawerState.open()
                            }
                        },
                        onMenuClick = {
                            // TODO: :-()
                        }
                    )
                }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    content()
                }
            }
        }
    }
}

@Composable
private fun NavigationDrawerItemComponent(
    item: NavigationDrawerItems,
    currentScreenRoute: String,
    navigateTo: (Screen) -> Unit,
) {
    NavigationDrawerItem(
        label = { Text(item.label) },
        badge = { if (currentScreenRoute == item.screen.route) Text("<") },
        selected = currentScreenRoute == item.screen.route,
        onClick = { navigateTo(item.screen) },
        icon = { Icon(item.icon, contentDescription = null) })
}

@Preview(
    widthDp = 700, showBackground = true
)
@Composable
private fun NavigationDrawerPreview() {
    val drawerState = rememberDrawerState(DrawerValue.Open)

    AppTheme {
        NavigationDrawer(
            drawerState = drawerState,
            navigateTo = {},
            currentScreenRoute = Screen.Home.route,
        ) {
            Text(text = "Контент. Очень длинный контент. Прям чтобы его было видно. Нужно прям много контента.")
        }
    }
}