package ru.bratusev.smartlab

import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.core.module.Module
import ru.bratusev.smartlab.feature_home.HomeScreen


@Composable
@Preview
fun App(
    platformModule: Module = Module()
) {
    KoinApplication(
        application = {
            modules(appModule, platformModule)
        }
    ) {
        HomeScreen()
    }
}