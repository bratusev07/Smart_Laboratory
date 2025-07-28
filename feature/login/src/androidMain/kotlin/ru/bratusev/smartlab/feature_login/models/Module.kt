package ru.bratusev.smartlab.feature_login.models

import org.koin.dsl.module

val androidModule = module {
    single { Device(context = get()) }
}

actual val platformModule = androidModule