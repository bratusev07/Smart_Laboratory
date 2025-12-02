package ru.bratusev.smartlab.data.core

import org.koin.dsl.module
import ru.bratusev.smartlab.data.core.dataStore.DataStoreFactory
import ru.bratusev.smartlab.data.core.database.DatabaseFactory
import ru.bratusev.smartlab.data.core.repository.VpnRepositoryImpl
import ru.bratusev.smartlab.domain.core.repository.VpnRepository

val iosDataModule = module {
    single<DataStoreFactory> {
        DataStoreFactory()
    }
    single<Logger> {
        Logger()
    }

    single <VpnRepository> {
        VpnRepositoryImpl()
    }

    single { DatabaseFactory() }
}

actual val platformDataModule = iosDataModule