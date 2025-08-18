package ru.bratusev.smartlab.feature_login.models

import org.koin.dsl.module

val androidLoginModule = module {
    single { Device(context = get()) }
}

val androidLoginModulePreview = module {
    single { Device(context = get()) }
}

actual val platformLoginModule = androidLoginModule
actual val platformLoginModulePreview= androidLoginModulePreview