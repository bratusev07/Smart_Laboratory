package ru.bratusev.smartlab.data.core.local_storage.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.formUrlEncode
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ru.bratusev.smartlab.data.core.remote_storage.Constants

object AuthTokensStore {

    private val KEY_ACCESS = stringPreferencesKey("auth_access_token")
    private val KEY_REFRESH = stringPreferencesKey("auth_refresh_token")
    private val KEY_EXPIRES_AT = longPreferencesKey("auth_expires_at_ms")

    suspend fun saveTokens(
        dataStore: DataStore<Preferences>,
        accessToken: String,
        refreshToken: String?,
        expiresInSeconds: Int?
    ) {
        val now = Clock.System.now().toEpochMilliseconds()
        val lifetimeMs = (expiresInSeconds?.toLong() ?: 0L) * 1000L
        val safetySkewMs = 30_000L
        val expiresAt = if (lifetimeMs > 0L) now + lifetimeMs - safetySkewMs else 0L

        dataStore.edit { prefs ->
            prefs[KEY_ACCESS] = accessToken
            refreshToken?.let { prefs[KEY_REFRESH] = it }
            if (expiresAt > 0L) prefs[KEY_EXPIRES_AT] = expiresAt
        }
    }

    suspend fun loadTokens(dataStore: DataStore<Preferences>): BearerTokens? {
        val prefs = dataStore.data.first()
        val access = prefs[KEY_ACCESS]
        val refresh = prefs[KEY_REFRESH]
        val expiresAt = prefs[KEY_EXPIRES_AT] ?: 0L
        val now = Clock.System.now().toEpochMilliseconds()
        if (access.isNullOrBlank()) return null
        if (expiresAt != 0L && now >= expiresAt) return null
        return BearerTokens(accessToken = access, refreshToken = refresh ?: "")
    }

    suspend fun refreshTokens(client: HttpClient, dataStore: DataStore<Preferences>): BearerTokens? {
        val prefs = dataStore.data.first()
        val refresh = prefs[KEY_REFRESH] ?: return null

        val body = Parameters.Companion.build {
            append("grant_type", "refresh_token")
            append("refresh_token", refresh)
            append("client_id", "https://home-assistant.io/android")
        }.formUrlEncode()

        val response = client.post("${Constants.BASE_URL}/auth/token") {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(body)
        }

        val json = Json.Default.parseToJsonElement(response.bodyAsText()).jsonObject
        val newAccess = json["access_token"]?.jsonPrimitive?.content ?: return null
        val newRefresh = json["refresh_token"]?.jsonPrimitive?.content ?: refresh
        val expiresIn = json["expires_in"]?.jsonPrimitive?.intOrNull

        saveTokens(
            dataStore = dataStore,
            accessToken = newAccess,
            refreshToken = newRefresh,
            expiresInSeconds = expiresIn
        )

        return BearerTokens(accessToken = newAccess, refreshToken = newRefresh)
    }
}