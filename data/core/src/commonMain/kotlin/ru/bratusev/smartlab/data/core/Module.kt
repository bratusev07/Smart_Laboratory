package ru.bratusev.smartlab.data.core

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.Module
import org.koin.dsl.module
import ru.bratusev.smartlab.data.core.dataStore.DataStoreFactory
import ru.bratusev.smartlab.data.core.dataStore.preview.DataStoreFactoryPreview
import ru.bratusev.smartlab.data.core.database.AppDatabase
import ru.bratusev.smartlab.data.core.database.DatabaseFactory
import ru.bratusev.smartlab.data.core.database.LogcatMessageDao
import ru.bratusev.smartlab.data.core.preview.KtorClientFactoryPreview
import ru.bratusev.smartlab.data.core.repository.AuthRepositoryImpl
import ru.bratusev.smartlab.data.core.repository.ButtonTextRepositoryImpl
import ru.bratusev.smartlab.data.core.repository.LoggerRepositoryImpl
import ru.bratusev.smartlab.data.core.repository.SocketRepositoryImpl
import ru.bratusev.smartlab.data.core.repository.preview.AuthRepositoryPreview
import ru.bratusev.smartlab.data.core.repository.preview.ButtonTextRepositoryPreview
import ru.bratusev.smartlab.data.core.repository.preview.LoggerRepositoryPreview
import ru.bratusev.smartlab.domain.core.repository.AuthRepository
import ru.bratusev.smartlab.domain.core.repository.ButtonTextRepository
import ru.bratusev.smartlab.domain.core.repository.LoggerRepository
import ru.bratusev.smartlab.domain.core.repository.SocketRepository

val dataModule = module {

    single<ButtonTextRepository> {
        ButtonTextRepositoryImpl()
    }

    single<CoroutineScope> {
        CoroutineScope(SupervisorJob() + Dispatchers.Main)
    }

    single<SocketRepository> {
        SocketRepositoryImpl(
            webSocketClient = get()
        )
    }

    single<AuthRepository> {
        AuthRepositoryImpl(
            client = get(),
            dataStore = get(),
            socketClient = get()
        )
    }

    single<LoggerRepository> {
        LoggerRepositoryImpl(
            logger = get(),
            logcatMessageDao = get(),
            coroutineScope = get()
        )
    }

    single<KtorClientFactory> {
        KtorClientFactory()
    }

    single<HttpClient> {
        get<KtorClientFactory>().createClient()
    }

    single<DataStore<Preferences>> {
        get<DataStoreFactory>().createDataStore()
    }

    single<HomeAssistantWebSocketClient> {
        HomeAssistantWebSocketClient()
    }

    single {
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }

    single<LogcatMessageDao> { get<AppDatabase>().logcatMessagesDao() }
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

    single {
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }

    single<CoroutineScope> {
        CoroutineScope(SupervisorJob() + Dispatchers.Main)
    }

    single<SocketRepository> {
        SocketRepositoryImpl(
            webSocketClient = get()
        )
    }

    single<LogcatMessageDao> { get<AppDatabase>().logcatMessagesDao() }

    includes(platformDataModulePreview)
}
expect val platformDataModule: Module
expect val platformDataModulePreview: Module