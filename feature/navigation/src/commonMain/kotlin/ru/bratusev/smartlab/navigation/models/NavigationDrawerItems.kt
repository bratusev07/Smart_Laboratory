package ru.bratusev.smartlab.navigation.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.ViewModule
import androidx.compose.ui.graphics.vector.ImageVector
import ru.bratusev.smartlab.navigation.api.Screen

enum class NavigationDrawerItems(val screen: Screen, val label: String, val icon: ImageVector) {
    Home(Screen.Home, "Доска", Icons.Default.Dashboard),

    CustomScreen(Screen.CustomScreen.Main, "Своя панель", Icons.Default.AppRegistration),
    Logs(Screen.Logcat, "Логи", Icons.AutoMirrored.Filled.List),
    Areas(Screen.Areas, "Пространства", Icons.Outlined.ViewModule),
    Settings(Screen.Settings, "Настройки", Icons.Default.Settings),
    Notifications(Screen.Notifications, "Уведомления", Icons.Default.Notifications),
    Profile(Screen.UserProfile, "<username>", Icons.Default.AccountCircle),
    Login(Screen.Login, "Выйти", Icons.AutoMirrored.Filled.Login)
}