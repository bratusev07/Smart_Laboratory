package ru.bratusev.smartlab.data.core

import org.koin.dsl.module
import ru.bratusev.smartlab.data.core.dataStore.DataStoreFactory
import ru.bratusev.smartlab.data.core.dataStore.preview.DataStoreFactoryPreview

val androidModule = module {
    single<DataStoreFactory> {
        DataStoreFactory(
            context = get()
        )
    }
}

val androidModulePreview = module {
    single<DataStoreFactoryPreview> {
        DataStoreFactoryPreview(
            context = get()
        )
    }
}

actual val platformModule = androidModule
actual val platformModulePreview = androidModulePreview