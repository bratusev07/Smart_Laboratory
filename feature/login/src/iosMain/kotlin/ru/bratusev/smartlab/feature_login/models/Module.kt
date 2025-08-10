package ru.bratusev.smartlab.feature_login.models

import org.koin.dsl.module

val iosLoginModule = module {
    single { Device() }
}

val iosLoginModulePreview = module {
    single {Device()}
}

actual val platformLoginModule = iosLoginModule
actual val platformLoginModulePreview = iosLoginModulePreview