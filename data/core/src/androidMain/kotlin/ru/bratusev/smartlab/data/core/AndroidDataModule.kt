package ru.bratusev.smartlab.data.core

import android.content.Context
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import ru.bratusev.smartlab.data.core.dataStore.DataStoreFactory
import ru.bratusev.smartlab.data.core.database.DatabaseFactory
import ru.bratusev.smartlab.data.core.repository.VpnRepositoryImpl
import ru.bratusev.smartlab.domain.core.repository.VpnRepository

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
    single<VpnRepository> { VpnRepositoryImpl(get()) }
}

actual val platformDataModule = androidDataModule