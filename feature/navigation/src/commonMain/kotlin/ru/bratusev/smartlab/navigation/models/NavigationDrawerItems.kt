package ru.bratusev.smartlab.navigation.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.ui.graphics.vector.ImageVector
import ru.bratusev.smartlab.navigation.Screen

// В Drawer все отображается в таком же порядке
enum class NavigationDrawerItems(val screen: Screen, val label: String, val icon: ImageVector) {
    Home(Screen.Home, "Доска", Icons.Default.Dashboard),
    Settings(Screen.Settings, "Логи", Icons.AutoMirrored.Filled.List),
    Login(Screen.Login, "Выйти", Icons.AutoMirrored.Filled.Login)
    // Пока подразумеваю, что это также кнопка выхода. Должно быть внизу.
}