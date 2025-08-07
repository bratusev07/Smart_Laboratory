package ru.bratusev.smartlab.domain.core

import org.koin.dsl.module
import ru.bratusev.smartlab.domain.core.usecase.GetButtonTextUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetLogcatMessagesUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetLoggerUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetLoginUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetServiceEntitiesUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetTokenUseCase
import ru.bratusev.smartlab.domain.core.usecase.ObserveSocketErrorsUseCase
import ru.bratusev.smartlab.domain.core.usecase.UpdateSwitchStateUseCase

val domainModule = module {

    factory<GetButtonTextUseCase> { GetButtonTextUseCase(buttonTextRepository = get()) }

    factory<GetTokenUseCase> { GetTokenUseCase(get()) }
    factory<GetLoginUseCase> { GetLoginUseCase(get()) }
    factory<GetLoggerUseCase> { GetLoggerUseCase(get()) }
    factory<UpdateSwitchStateUseCase> { UpdateSwitchStateUseCase(get()) }
    factory<GetServiceEntitiesUseCase> { GetServiceEntitiesUseCase(get()) }
    factory<ObserveSocketErrorsUseCase> { ObserveSocketErrorsUseCase(get()) }
    factory<GetLogcatMessagesUseCase> { GetLogcatMessagesUseCase(get()) }
}

val domainModulePreview = module {
    factory<GetButtonTextUseCase> { GetButtonTextUseCase(buttonTextRepository = get()) }

    factory<GetTokenUseCase> { GetTokenUseCase(get()) }
    factory<GetLoginUseCase> { GetLoginUseCase(get()) }
    factory<GetLoggerUseCase> { GetLoggerUseCase(get()) }
    factory<UpdateSwitchStateUseCase> { UpdateSwitchStateUseCase(get()) }
    factory<GetServiceEntitiesUseCase> { GetServiceEntitiesUseCase(get()) }
    factory<ObserveSocketErrorsUseCase> { ObserveSocketErrorsUseCase(get()) }
    factory<GetLogcatMessagesUseCase> { GetLogcatMessagesUseCase(get()) }
}
