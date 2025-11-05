package ru.bratusev.smartlab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ru.bratusev.smartlab.navigation.AppNavigation
import ru.bratusev.smartlab.ui.core.theme.AppTheme
import smartlaboratory.composeapp.generated.resources.Res
import smartlaboratory.composeapp.generated.resources.compose_multiplatform
import smartlaboratory.ui.core.generated.resources.loading_settings

@Composable
fun App(vm: AppViewModel = koinViewModel()) {
    val state by vm.uiState.collectAsState()
    customAppLocale = if (state.isoLangName == "system") null else state.isoLangName
    println("Current language is ${state.isoLangName}")
    AppTheme(
        darkTheme =
            state.isDarkTheme
    ) {
        Surface {
            if (state.isLoadingSettings) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Text(stringResource(smartlaboratory.ui.core.generated.resources.Res.string.loading_settings))
                }
            } else {
                val navController = rememberNavController()
                AppEnvironment {
                    Box {
                        AppNavigation(navController = navController)
                    }
                }
                Res.drawable.compose_multiplatform
            }
        }
    }
}
