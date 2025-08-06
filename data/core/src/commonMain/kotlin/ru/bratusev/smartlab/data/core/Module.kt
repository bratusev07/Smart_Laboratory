package ru.bratusev.smartlab.data.core

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.dsl.module
import ru.bratusev.smartlab.data.core.dataStore.DataStoreFactory
import ru.bratusev.smartlab.data.core.repository.AuthRepositoryImpl
import ru.bratusev.smartlab.data.core.repository.ButtonTextRepositoryImpl
import ru.bratusev.smartlab.data.core.repository.LoggerRepositoryImpl
import ru.bratusev.smartlab.domain.core.repository.AuthRepository
import ru.bratusev.smartlab.domain.core.repository.ButtonTextRepository
import ru.bratusev.smartlab.domain.core.repository.LoggerRepository

val dataModule = module {

    single<ButtonTextRepository> {
        ButtonTextRepositoryImpl()
    }

    single<AuthRepository> {
        AuthRepositoryImpl(
            client = get(),
            socketClient = get(),
            dataStore = get()
        )
    }

    single<LoggerRepository> {
        LoggerRepositoryImpl(get())
    }

    single<KtorClientFactory> {
        KtorClientFactory()
    }

    single<HttpClient> {
        get<KtorClientFactory>().createClient()
    }
    includes(platformModule)

    single<DataStore<Preferences>> {
        get<DataStoreFactory>().createDataStore()
    }

    single<HomeAssistantWebSocketClient> {
        HomeAssistantWebSocketClient()
    }
}
expect val platformModule: Module
