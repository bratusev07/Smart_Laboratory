package ru.bratusev.smartlab.feature_settings

import org.koin.dsl.module
import ru.bratusev.smartlab.data.core.dataModule
import ru.bratusev.smartlab.domain.core.domainModule

val settingsModule = module {
    includes(domainModule, dataModule)
}