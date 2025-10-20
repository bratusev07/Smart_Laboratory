package ru.bratusev.smartlab.data.core

import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import ru.bratusev.smartlab.data.core.dataStore.DataStoreFactory
import ru.bratusev.smartlab.data.core.database.DatabaseFactory

val androidDataModule = module {
    single<DataStoreFactory> {
        DataStoreFactory(
            context = get()
        )
    }
    single<Logger> {
        Logger()
    }

    single { DatabaseFactory(androidApplication()) }
}

actual val platformDataModule = androidDataModule