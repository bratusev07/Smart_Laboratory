package ru.bratusev.smartlab.di

import org.koin.dsl.module
import ru.bratusev.smartlab.navigation.navigationModule
import ru.bratusev.smartlab.navigation.navigationModulePreview

val appModule = module {
    includes(navigationModule)
}

val appModulePreview = module {
    includes(navigationModulePreview)
}