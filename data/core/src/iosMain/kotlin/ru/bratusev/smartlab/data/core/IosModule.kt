package ru.bratusev.smartlab.data.core

import org.koin.dsl.module
import ru.bratusev.smartlab.data.core.dataStore.DataStoreFactory
import ru.bratusev.smartlab.data.core.database.AppDatabase
import ru.bratusev.smartlab.data.core.database.DatabaseFactory

val iosModule = module {
    single<DataStoreFactory> {
        DataStoreFactory()
    }
    single<Logger>{
        Logger()
    }

    single { DatabaseFactory() }
}

actual val platformModule = iosModule