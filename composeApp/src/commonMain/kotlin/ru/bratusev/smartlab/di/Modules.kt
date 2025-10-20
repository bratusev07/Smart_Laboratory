package ru.bratusev.smartlab.di

import org.koin.dsl.module
import ru.bratusev.smartlab.navigation.navigationModule

val appModule = module {
    includes(navigationModule)
}