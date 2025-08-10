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
import ru.bratusev.smartlab.data.core.repository.LoggerRepositoryImpl
import ru.bratusev.smartlab.data.core.repository.preview.AuthRepositoryPreview
import ru.bratusev.smartlab.data.core.repository.preview.ButtonTextRepositoryPreview
import ru.bratusev.smartlab.data.core.repository.preview.LoggerRepositoryPreview
import ru.bratusev.smartlab.domain.core.repository.AuthRepository
import ru.bratusev.smartlab.domain.core.repository.ButtonTextRepository
import ru.bratusev.smartlab.domain.core.repository.LoggerRepository

val dataModule = module {

    single<ButtonTextRepository> {
        ButtonTextRepositoryImpl()
    }

    single<AuthRepository> {
        AuthRepositoryImpl(get(), get(), get())
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

    single<HomeAssistantWebSocketClient> {
        HomeAssistantWebSocketClient()
    }

    single<DataStore<Preferences>> {
        get<DataStoreFactory>().createDataStore()
    }

    includes(platformDataModule)
}

val dataModulePreview = module {

    single<ButtonTextRepository> {
        ButtonTextRepositoryPreview()
    }

    single<AuthRepository> {
        AuthRepositoryPreview(
            client = get(), dataStore = get()
        )
    }

    single<LoggerRepository> {
        LoggerRepositoryPreview(get())
    }

    single<KtorClientFactoryPreview> {
        KtorClientFactoryPreview()
    }

    single<HttpClient> {
        get<KtorClientFactoryPreview>().createClient()
    }

    single<DataStore<Preferences>> {
        get<DataStoreFactoryPreview>().createDataStore()
    }

    single<HomeAssistantWebSocketClient> {
        HomeAssistantWebSocketClient()
    }

    includes(platformDataModulePreview)
}
expect val platformDataModule: Module
expect val platformDataModulePreview: Module