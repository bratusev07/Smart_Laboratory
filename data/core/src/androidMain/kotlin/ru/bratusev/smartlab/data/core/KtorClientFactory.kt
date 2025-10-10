package ru.bratusev.smartlab.data.core

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.bratusev.smartlab.data.core.auth.AuthTokensStore

actual class KtorClientFactory : KoinComponent {
    actual fun createClient(): HttpClient {
        val dataStore: DataStore<Preferences> by inject()
        return HttpClient(Android) {
            install(Auth) {
                bearer {
                    loadTokens {
                        AuthTokensStore.loadTokens(dataStore)
                    }
                    refreshTokens {
                        AuthTokensStore.refreshTokens(client, dataStore)
                    }
                    sendWithoutRequest { request ->
                        // Send Authorization header for API endpoints
                        request.url.host == java.net.URL(Constants.BASE_URL).host
                    }
                }
            }
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            install(Logging) {
                level = LogLevel.ALL
            }
        }
    }
} 