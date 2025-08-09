package ru.bratusev.smartlab.data.core

import org.koin.dsl.module
import ru.bratusev.smartlab.data.core.dataStore.DataStoreFactory
import ru.bratusev.smartlab.data.core.dataStore.preview.DataStoreFactory

val iosModule = module {
    single<DataStoreFactory> {
        DataStoreFactory()
    }
    single<Logger>{
        Logger()
    }
}

val iosModulePreview = module {
    single<ru.bratusev.smartlab.data.core.dataStore.preview.DataStoreFactory> {
        ru.bratusev.smartlab.data.core.dataStore.preview.DataStoreFactory()
    }
}

actual val platformModule = iosModule
actual val platformModulePreview = iosModulePreview