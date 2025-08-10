package ru.bratusev.smartlab.data.core

import org.koin.dsl.module
import ru.bratusev.smartlab.data.core.dataStore.DataStoreFactory
import ru.bratusev.smartlab.data.core.dataStore.preview.DataStoreFactoryPreview
import ru.bratusev.smartlab.data.core.preview.LoggerPreview

val androidDataModule = module {
    single<DataStoreFactory> {
        DataStoreFactory(
            context = get()
        )
    }
    single<Logger> {
        Logger()
    }
}

val androidDataModulePreview = module {
    single<DataStoreFactoryPreview> {
        DataStoreFactoryPreview(
            context = get()
        )
    }
    single<LoggerPreview> {
        LoggerPreview()
    }
}

actual val platformDataModule = androidDataModule
actual val platformDataModulePreview = androidDataModulePreview