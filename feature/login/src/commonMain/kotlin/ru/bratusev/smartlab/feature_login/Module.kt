package ru.bratusev.smartlab.feature_login

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.bratusev.smartlab.data.core.dataModule
import ru.bratusev.smartlab.domain.core.domainModule
import ru.bratusev.smartlab.feature_login.models.platformLoginModule

val loginModule = module {
    includes(domainModule, dataModule, platformLoginModule)
    viewModelOf(::LoginViewModel)
}