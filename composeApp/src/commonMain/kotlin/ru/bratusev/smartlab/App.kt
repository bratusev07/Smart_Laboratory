package ru.bratusev.smartlab

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.navigation.AppNavigation
import ru.bratusev.smartlab.ui.core.theme.AppTheme

@Composable
@Preview
fun App() {
    AppTheme {
        val navController = rememberNavController()
        AppNavigation(navController = navController)
    }
}