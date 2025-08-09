package ru.bratusev.smartlab.data.core

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.dsl.module
import ru.bratusev.smartlab.data.core.dataStore.DataStoreFactory
import ru.bratusev.smartlab.data.core.dataStore.preview.DataStoreFactoryPreview
import ru.bratusev.smartlab.data.core.preview.KtorClientFactoryPreview
import ru.bratusev.smartlab.data.core.repository.AuthRepositoryImpl
import ru.bratusev.smartlab.data.core.repository.ButtonTextRepositoryImpl
import ru.bratusev.smartlab.data.core.repository.preview.AuthRepositoryPreview
import ru.bratusev.smartlab.data.core.repository.preview.ButtonTextRepositoryPreview
import ru.bratusev.smartlab.data.core.repository.LoggerRepositoryImpl
import ru.bratusev.smartlab.domain.core.repository.AuthRepository
import ru.bratusev.smartlab.domain.core.repository.ButtonTextRepository
import ru.bratusev.smartlab.domain.core.repository.LoggerRepository

val dataModule = module {

    single<ButtonTextRepository> {
        ButtonTextRepositoryImpl()
    }

    single<AuthRepository> {
        AuthRepositoryImpl(get())
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

val dataModulePreview = module {

    single<ButtonTextRepository> {
        ButtonTextRepositoryPreview()
    }

    single<AuthRepository> {
        AuthRepositoryPreview(
            client = get(),
            dataStore = get()
        )
    }

    single<KtorClientFactoryPreview> {
        KtorClientFactoryPreview()
    }

    single<HttpClient> {
        get<KtorClientFactoryPreview>().createClient()
    }
    includes(platformModule)

    single<DataStore<Preferences>> {
        get<DataStoreFactoryPreview>().createDataStore()
    }
}
expect val platformModule: Module
expect val platformModulePreview: Module