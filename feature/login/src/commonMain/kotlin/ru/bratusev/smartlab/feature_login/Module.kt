package ru.bratusev.smartlab.feature_login

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.bratusev.smartlab.data.core.dataModule
import ru.bratusev.smartlab.data.core.dataModulePreview
import ru.bratusev.smartlab.domain.core.domainModule
import ru.bratusev.smartlab.domain.core.domainModulePreview
import ru.bratusev.smartlab.feature_login.models.platformLoginModule
import ru.bratusev.smartlab.feature_login.models.platformLoginModulePreview

val loginModule = module {
    includes(domainModule, dataModule, platformLoginModule)
    viewModelOf(::LoginViewModel)
}

val loginModulePreview = module {
    includes(domainModulePreview, dataModulePreview, platformLoginModulePreview)
    viewModelOf(::LoginViewModel)
}