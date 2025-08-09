package ru.bratusev.smartlab.feature_home

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.bratusev.smartlab.data.core.dataModule
import ru.bratusev.smartlab.data.core.dataModulePreview
import ru.bratusev.smartlab.domain.core.domainModule

val homeModule = module {
    includes(domainModule, dataModule)

    viewModelOf(::HomeViewModel)
}

val homeModulePreview = module {
    includes(domainModule, dataModulePreview)
    viewModelOf(::HomeViewModel)
}