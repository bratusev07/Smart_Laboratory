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
import org.jetbrains.compose.resources.StringResource
import ru.bratusev.smartlab.navigation.api.Screen
import smartlaboratory.ui.core.generated.resources.Res
import smartlaboratory.ui.core.generated.resources.drawer_custom_screen
import smartlaboratory.ui.core.generated.resources.drawer_dashboard
import smartlaboratory.ui.core.generated.resources.drawer_logs
import smartlaboratory.ui.core.generated.resources.drawer_notifications
import smartlaboratory.ui.core.generated.resources.drawer_profile
import smartlaboratory.ui.core.generated.resources.drawer_quit
import smartlaboratory.ui.core.generated.resources.drawer_settings
import smartlaboratory.ui.core.generated.resources.drawer_zones

enum class NavigationDrawerItems(val screen: Screen, val labelRes: StringResource, val icon: ImageVector) {
    Home(Screen.Home, Res.string.drawer_dashboard, Icons.Default.Dashboard),

    CustomScreen(Screen.CustomScreen.Root, Res.string.drawer_custom_screen, Icons.Default.AppRegistration),
    Logs(Screen.Logcat, Res.string.drawer_logs, Icons.AutoMirrored.Filled.List),
    Areas(Screen.Areas.Root, Res.string.drawer_zones, Icons.Outlined.ViewModule),
    Settings(Screen.Settings, Res.string.drawer_settings, Icons.Default.Settings),
    Notifications(Screen.Notifications, Res.string.drawer_notifications, Icons.Default.Notifications),
    Profile(Screen.UserProfile, Res.string.drawer_profile, Icons.Default.AccountCircle),
    Login(Screen.Login, Res.string.drawer_quit, Icons.AutoMirrored.Filled.Login)
}