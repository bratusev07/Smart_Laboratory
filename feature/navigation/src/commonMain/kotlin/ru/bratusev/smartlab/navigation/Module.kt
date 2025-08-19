package ru.bratusev.smartlab.navigation

import org.koin.dsl.module
import ru.bratusev.smartlab.feature_customScreen.customScreenModule
import ru.bratusev.smartlab.feature_customScreen.customScreenModulePreview
import ru.bratusev.smartlab.feature_home.homeModule
import ru.bratusev.smartlab.feature_home.homeModulePreview
import ru.bratusev.smartlab.feature_logcat.logcatModule
import ru.bratusev.smartlab.feature_logcat.logcatModulePreview
import ru.bratusev.smartlab.feature_login.loginModule
import ru.bratusev.smartlab.feature_login.loginModulePreview
import ru.bratusev.smartlab.feature_settings.settingsModule
import ru.bratusev.smartlab.feature_settings.settingsModulePreview

val navigationModule = module {
    includes(homeModule, settingsModule, loginModule, logcatModule, customScreenModule)
}

val navigationModulePreview = module {
    includes(
        homeModulePreview,
        settingsModulePreview,
        loginModulePreview,
        logcatModulePreview,
        customScreenModulePreview
    )
}