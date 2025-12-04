package ru.bratusev.smartlab.data.core

import android.content.Context
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import ru.bratusev.smartlab.data.core.dataStore.DataStoreFactory
import ru.bratusev.smartlab.data.core.database.DatabaseFactory
import ru.bratusev.smartlab.data.core.repository.NetworkRepositoryImpl
import ru.bratusev.smartlab.domain.core.repository.NetworkRepository

val androidDataModule = module {
    single <Context> {
        androidApplication()
    }

    single<DataStoreFactory> {
        DataStoreFactory(
            context = get()
        )
    }
    single<Logger> {
        Logger()
    }

    single<DatabaseFactory> { DatabaseFactory(get()) }
    single<NetworkRepository> { NetworkRepositoryImpl(get()) }
}

actual val platformDataModule = androidDataModule