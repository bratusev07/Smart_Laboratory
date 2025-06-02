package ru.bratusev.smartlab.domain.core

import org.koin.dsl.module
import ru.bratusev.smartlab.domain.core.usecase.GetButtonTextUseCase

val domainModule = module {

    factory { GetButtonTextUseCase(buttonTextRepository = get()) }
}