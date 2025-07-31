package ru.bratusev.smartlab.domain.core

import org.koin.dsl.module
import ru.bratusev.smartlab.domain.core.usecase.GetButtonTextUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetLoggerUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetTokenUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetLoginUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetSaveTokenUseCase

val domainModule = module {

    factory<GetButtonTextUseCase> { GetButtonTextUseCase(buttonTextRepository = get()) }


    factory<GetTokenUseCase> { GetTokenUseCase(get()) }
    factory<GetSaveTokenUseCase> { GetSaveTokenUseCase(get()) }
    factory<GetLoginUseCase> { GetLoginUseCase(authRepository = get()) }
    factory<GetLoggerUseCase> { GetLoggerUseCase(get()) }
}