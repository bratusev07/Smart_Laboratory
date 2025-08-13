package ru.bratusev.smartlab.navigation.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.ui.graphics.vector.ImageVector
import ru.bratusev.smartlab.navigation.Screen

enum class NavigationDrawerItems(val screen: Screen, val label: String, val icon: ImageVector) {
    Home(Screen.Home, "Доска", Icons.Default.Dashboard),
    Settings(Screen.Settings, "Логи", Icons.AutoMirrored.Filled.List),
    Login(Screen.Login, "Авторизация", Icons.AutoMirrored.Filled.Login)
}