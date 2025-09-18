package ru.bratusev.smartlab

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import ru.bratusev.smartlab.navigation.AppNavigation
import ru.bratusev.smartlab.ui.core.theme.AppTheme
import smartlaboratory.composeapp.generated.resources.Res
import smartlaboratory.composeapp.generated.resources.compose_multiplatform

@Composable
fun App() {
    AppTheme {
        val navController = rememberNavController()
        Surface {
            Box {
                AppNavigation(navController = navController)
            }
        }
        Res.drawable.compose_multiplatform
    }
}