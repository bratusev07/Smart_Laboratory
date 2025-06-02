package ru.bratusev.smartlab

import org.koin.dsl.module
import ru.bratusev.smartlab.feature_home.homeModule
import ru.bratusev.smartlab.feature_settings.settingsModule

val appModule = module {
    includes(
        homeModule,
        settingsModule
    )
}