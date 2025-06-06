package ru.bratusev.smartlab

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.navigation.AppNavigation

@Composable
@Preview
fun App() {
    val navController = rememberNavController()
    AppNavigation(navController = navController)
}