package ru.bratusev.smartlab.data.core

import org.koin.dsl.module
import ru.bratusev.smartlab.data.core.dataStore.DataStoreFactory

val iosModule = module {
    single<DataStoreFactory> {
        DataStoreFactory()
    }
}

actual val platformModule = iosModule