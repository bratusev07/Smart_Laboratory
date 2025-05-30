package ru.bratusev.smartlab.feature_home

import org.koin.dsl.module
import ru.bratusev.smartlab.domain.core.domainModule

val homeModule = module {

    includes(domainModule)
}