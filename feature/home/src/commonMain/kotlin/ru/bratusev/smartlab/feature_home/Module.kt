package ru.bratusev.smartlab.feature_home

import org.koin.dsl.module
import ru.bratusev.smartlab.data.core.dataModule
import ru.bratusev.smartlab.domain.core.domainModule

val homeModule = module {
    includes(domainModule, dataModule)
}