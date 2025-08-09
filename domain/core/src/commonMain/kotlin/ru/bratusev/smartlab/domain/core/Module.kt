package ru.bratusev.smartlab.domain.core

import org.koin.dsl.module
import ru.bratusev.smartlab.domain.core.usecase.GetButtonTextUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetTokenUseCase
import ru.bratusev.smartlab.domain.core.usecase.LoginUseCase
import ru.bratusev.smartlab.domain.core.usecase.SaveTokenUseCase

val domainModule = module {

    factory<GetButtonTextUseCase> { GetButtonTextUseCase(buttonTextRepository = get()) }


    factory<GetTokenUseCase> { GetTokenUseCase(get()) }
    factory<SaveTokenUseCase> { SaveTokenUseCase(get()) }
    factory<LoginUseCase> { LoginUseCase(authRepository = get()) }
}
