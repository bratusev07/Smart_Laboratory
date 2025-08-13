package ru.bratusev.smartlab.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
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
import ru.bratusev.smartlab.navigation.models.NavigationDrawerItems
import ru.bratusev.smartlab.ui.core.resources.StringsRes
import ru.bratusev.smartlab.ui.core.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer(
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    scope: CoroutineScope = rememberCoroutineScope(),
    isHidden: Boolean = false,
    navigateTo: (Screen) -> Unit,
    selectedScreenRoute: String,
    content: @Composable () -> Unit,
) {
    ModalNavigationDrawer(
        modifier = Modifier.wrapContentWidth(), gesturesEnabled = !isHidden, drawerContent = {
            if (!isHidden) {
                ModalDrawerSheet {
                    Column(
                        modifier = Modifier.fillMaxHeight().verticalScroll(rememberScrollState())
                    ) {
                        Spacer(Modifier.height(12.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            IconButton(onClick = {
                                scope.launch {
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
                        // TODO сделать получше
                        NavigationDrawerItems.entries.forEach {
                            if (it == NavigationDrawerItems.Login){
                                Spacer(modifier = Modifier.weight(1f))
                                HorizontalDivider()
                            }
                            NavigationDrawerItem(
                                label = { Text(it.label) },
                                selected = it.screen.route == selectedScreenRoute,
                                onClick = { navigateTo(it.screen) },
                                icon = { Icon(it.icon, contentDescription = null) }
                            )
                        }
                    }
                }
            }
        }, drawerState = drawerState
    ) {
        content()
    }
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
            selectedScreenRoute = Screen.Home.route,
        ) {
            Text(text = "Контент. Очень длинный контент. Прям чтобы его было видно. Нужно прям много контента.")
        }
    }
}