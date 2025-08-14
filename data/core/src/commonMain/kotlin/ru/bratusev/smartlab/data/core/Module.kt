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
import ru.bratusev.smartlab.data.core.database.AppDatabase
import ru.bratusev.smartlab.data.core.database.DatabaseFactory
import ru.bratusev.smartlab.data.core.database.LogcatMessageDao
import ru.bratusev.smartlab.data.core.repository.AuthRepositoryImpl
import ru.bratusev.smartlab.data.core.repository.ButtonTextRepositoryImpl
import ru.bratusev.smartlab.data.core.repository.LoggerRepositoryImpl
import ru.bratusev.smartlab.domain.core.repository.AuthRepository
import ru.bratusev.smartlab.domain.core.repository.ButtonTextRepository
import ru.bratusev.smartlab.domain.core.repository.LoggerRepository

val dataModule = module {

    single<CoroutineScope> {
        CoroutineScope(SupervisorJob() + Dispatchers.Main)
    }

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
    includes(platformModule)

    single<DataStore<Preferences>> {
        get<DataStoreFactory>().createDataStore()
    }

    single<HomeAssistantWebSocketClient> {
        HomeAssistantWebSocketClient()
    }

    // Database
    single {
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }

    single<LogcatMessageDao> { get<AppDatabase>().logcatMessagesDao() }

}
expect val platformModule: Module
