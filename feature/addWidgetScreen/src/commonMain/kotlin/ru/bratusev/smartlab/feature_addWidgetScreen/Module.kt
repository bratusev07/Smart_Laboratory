package ru.bratusev.smartlab.feature_addWidgetScreen

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.bratusev.smartlab.data.core.dataModule
import ru.bratusev.smartlab.data.core.dataModulePreview
import ru.bratusev.smartlab.domain.core.domainModule
import ru.bratusev.smartlab.domain.core.domainModulePreview

val addWidgetScreenModule = module {
    includes(domainModule, dataModule)

    viewModelOf(::AddWidgetScreenViewModel)
}

val addWidgetScreenPreviewModule = module {
    includes(domainModulePreview, dataModulePreview)

    viewModelOf(::AddWidgetScreenViewModel)
}