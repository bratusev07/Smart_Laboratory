package ru.bratusev.smartlab.data.core

import org.koin.dsl.module
import ru.bratusev.smartlab.data.core.dataStore.DataStoreFactory
import ru.bratusev.smartlab.data.core.dataStore.preview.DataStoreFactoryPreview

val iosDataModule = module {
    single<DataStoreFactory> {
        DataStoreFactory()
    }
    single<Logger> {
        Logger()
    }

    single { DatabaseFactory() }
}

val iosDataModulePreview = module {
    single<DataStoreFactoryPreview> {
        DataStoreFactoryPreview()
    }
    single<Logger> {
        Logger()
    }

    single { DatabaseFactory() }
}

actual val platformDataModule = iosDataModule
actual val platformDataModulePreview = iosDataModulePreview