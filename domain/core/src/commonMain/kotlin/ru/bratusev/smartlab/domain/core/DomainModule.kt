package ru.bratusev.smartlab.domain.core

import org.koin.dsl.module
import ru.bratusev.smartlab.domain.core.usecase.GetAreaDevicesUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetAreasUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetButtonTextUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetCustomWidgetsUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetLogcatMessagesUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetLoggerUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetLoginUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetServiceEntitiesUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetTokenUseCase
import ru.bratusev.smartlab.domain.core.usecase.ObserveSocketErrorsUseCase
import ru.bratusev.smartlab.domain.core.usecase.SetCustomWidgetsUseCase
import ru.bratusev.smartlab.domain.core.usecase.UpdateSensorUseCase

val domainModule = module {

    factory<GetButtonTextUseCase> { GetButtonTextUseCase(buttonTextRepository = get()) }

    factory<GetTokenUseCase> { GetTokenUseCase(get()) }
    factory<GetLoginUseCase> { GetLoginUseCase(get()) }
    factory<GetLoggerUseCase> { GetLoggerUseCase(get()) }
    factory<UpdateSensorUseCase> { UpdateSensorUseCase(get()) }
    factory<GetServiceEntitiesUseCase> { GetServiceEntitiesUseCase(get()) }
    factory<GetAreasUseCase> { GetAreasUseCase(get()) }
    factory<GetAreaDevicesUseCase> { GetAreaDevicesUseCase(get()) }
    factory<ObserveSocketErrorsUseCase> { ObserveSocketErrorsUseCase(get()) }
    factory<GetLogcatMessagesUseCase> { GetLogcatMessagesUseCase(get()) }
    factory<GetCustomWidgetsUseCase> { GetCustomWidgetsUseCase(get()) }
    factory<SetCustomWidgetsUseCase> { SetCustomWidgetsUseCase(get()) }
}

val domainModulePreview = module {
    factory<GetButtonTextUseCase> { GetButtonTextUseCase(buttonTextRepository = get()) }

    factory<GetTokenUseCase> { GetTokenUseCase(get()) }
    factory<GetLoginUseCase> { GetLoginUseCase(get()) }
    factory<GetLoggerUseCase> { GetLoggerUseCase(get()) }
    factory<GetAreaDevicesUseCase> { GetAreaDevicesUseCase(get()) }
    factory<UpdateSensorUseCase> { UpdateSensorUseCase(get()) }
    factory<GetServiceEntitiesUseCase> { GetServiceEntitiesUseCase(get()) }
    factory<ObserveSocketErrorsUseCase> { ObserveSocketErrorsUseCase(get()) }
    factory<GetLogcatMessagesUseCase> { GetLogcatMessagesUseCase(get()) }
    factory<GetCustomWidgetsUseCase> { GetCustomWidgetsUseCase(get()) }
    factory<SetCustomWidgetsUseCase> { SetCustomWidgetsUseCase(get()) }
}
