package ru.bratusev.smartlab.feature_login.models

import org.koin.dsl.module

val iosModule = module {
    single { Device() }
}

actual val platformModule = iosModule