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
import ru.bratusev.smartlab.data.core.local_storage.dataStore.DataStoreFactory
import ru.bratusev.smartlab.data.core.local_storage.database.AppDatabase
import ru.bratusev.smartlab.data.core.local_storage.database.DatabaseFactory
import ru.bratusev.smartlab.data.core.local_storage.database.LogcatMessageDao
import ru.bratusev.smartlab.data.core.remote_storage.HomeAssistantWebSocketClient
import ru.bratusev.smartlab.data.core.repository.AuthRepositoryImpl
import ru.bratusev.smartlab.data.core.repository.AutomationRepositoryImpl
import ru.bratusev.smartlab.data.core.repository.ButtonTextRepositoryImpl
import ru.bratusev.smartlab.data.core.repository.LoggerRepositoryImpl
import ru.bratusev.smartlab.data.core.repository.SettingsRepositoryImpl
import ru.bratusev.smartlab.data.core.repository.SocketRepositoryImpl
import ru.bratusev.smartlab.data.core.repository.WidgetRepositoryImpl
import ru.bratusev.smartlab.domain.core.repository.AuthRepository
import ru.bratusev.smartlab.domain.core.repository.AutomationRepository
import ru.bratusev.smartlab.domain.core.repository.ButtonTextRepository
import ru.bratusev.smartlab.domain.core.repository.LoggerRepository
import ru.bratusev.smartlab.domain.core.repository.SettingsRepository
import ru.bratusev.smartlab.domain.core.repository.SocketRepository
import ru.bratusev.smartlab.domain.core.repository.WidgetsRepository

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
            socketClient = get(),
            dataStore = get()
        )
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(
            dataStore = get()
        )
    }

    single<LoggerRepository> {
        LoggerRepositoryImpl(
            logger = get(), logcatMessageDao = get(), coroutineScope = get()
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
        get<DatabaseFactory>().create().setDriver(BundledSQLiteDriver()).build()
    }

    single<LogcatMessageDao> { get<AppDatabase>().logcatMessagesDao() }

    single<WidgetsRepository> { WidgetRepositoryImpl(get()) }

    single<AutomationRepository> { AutomationRepositoryImpl(get(), get()) }

    includes(platformDataModule)
}

expect val platformDataModule: Module