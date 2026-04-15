package ru.bratusev.smartlab.data.core

import org.koin.dsl.module
import ru.bratusev.smartlab.data.core.local_storage.dataStore.DataStoreFactory
import ru.bratusev.smartlab.data.core.local_storage.database.DatabaseFactory
import ru.bratusev.smartlab.data.core.repository.NetworkRepositoryImpl
import ru.bratusev.smartlab.domain.core.repository.NetworkRepository

val iosDataModule = module {
    single<DataStoreFactory> { DataStoreFactory() }
    single<Logger> { Logger() }

    single<NetworkRepository> { NetworkRepositoryImpl(get()) }

    single { DatabaseFactory() }
}

actual val platformDataModule = iosDataModule