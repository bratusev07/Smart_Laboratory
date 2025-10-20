package ru.bratusev.smartlab.feature_areas

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.bratusev.smartlab.data.core.dataModule
import ru.bratusev.smartlab.domain.core.domainModule

val allAreasScreenModule = module {
    includes(domainModule, dataModule)

    viewModelOf(::AllAreasScreenViewModel)
}