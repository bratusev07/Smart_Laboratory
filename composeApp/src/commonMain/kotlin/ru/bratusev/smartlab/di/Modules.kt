package ru.bratusev.smartlab.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.bratusev.smartlab.AppViewModel
import ru.bratusev.smartlab.navigation.navigationModule

val appModule = module {
    includes(navigationModule)

    viewModelOf(::AppViewModel)
}