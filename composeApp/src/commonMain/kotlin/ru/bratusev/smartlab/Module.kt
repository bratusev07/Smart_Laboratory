package ru.bratusev.smartlab

import org.koin.dsl.module
import ru.bratusev.smartlab.feature_home.homeModule

val appModule = module {
    includes(
        homeModule
    )
}