package ru.bratusev.smartlab.navigation

import org.koin.dsl.module
import ru.bratusev.smartlab.feature_home.homeModule
import ru.bratusev.smartlab.feature_settings.settingsModule

val navigationModule = module {
    includes(homeModule, settingsModule)
}