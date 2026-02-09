package ru.bratusev.smartlab.data.core.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import ru.bratusev.smartlab.data.core.model.ServerSelectionEntity
import ru.bratusev.smartlab.domain.core.model.ServerSelection
import ru.bratusev.smartlab.domain.core.repository.ServerSelectionRepository

class ServerSelectionRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
) : ServerSelectionRepository {

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    private val defaultServerSelection = ServerSelection(
        servers = mapOf(
            "http://10.131.170.77:8123" to "SKB",
            "Preview1" to "Preview1",
            "Preview2" to "Preview2",
            "Preview3" to "Preview3",
            "Preview4" to "Preview4",
            "Preview5" to "Preview5"
        ), currentServerUrl = null
    )

    private var isCachedBaseUrlUpToDate: Boolean = false
    private var cachedCurrentBaseUrl: String? = null

    override fun observerServerSelection(): Flow<ServerSelection> {
        return dataStore.data.map { preferences ->
            preferences[SERVER_SELECTION_KEY]
        }.map { jsonString ->
            if (jsonString.isNullOrBlank()) {
                println("ServerSelectionRepositoryImpl: DataStore is empty, returning default.")
                return@map defaultServerSelection
            }

            try {
                val entity = json.decodeFromString<ServerSelectionEntity>(jsonString)

                if (entity.servers.isEmpty()) {
                    defaultServerSelection
                } else {
                    entity.toDomain()
                }
            } catch (e: Exception) {
                println("ServerSelectionRepositoryImpl/observerServerSelection Got exception: $e")
                defaultServerSelection
            }
        }
    }

    override suspend fun setServerSelection(serverSelection: ServerSelection) {
        isCachedBaseUrlUpToDate = false
        val jsonString = json.encodeToString(
            ServerSelectionEntity.serializer(), ServerSelectionEntity.fromDomain(serverSelection)
        )
        dataStore.edit { preferences ->
            preferences[SERVER_SELECTION_KEY] = jsonString
        }
    }

    override fun getCurrentBaseUrl(): String? {
        if (isCachedBaseUrlUpToDate) {
            println("BaseUrl: $cachedCurrentBaseUrl")
            return cachedCurrentBaseUrl
        }

        cachedCurrentBaseUrl = runBlocking {
            val jsonString = dataStore.data.map { preferences ->
                preferences[SERVER_SELECTION_KEY]
            }.first()

            if (jsonString.isNullOrBlank()) {
                return@runBlocking defaultServerSelection.currentServerUrl
            }

            try {
                val serverSelection = json.decodeFromString<ServerSelectionEntity>(jsonString)
                serverSelection.currentServerUrl
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
        isCachedBaseUrlUpToDate = true
        println("BaseUrl: $cachedCurrentBaseUrl")
        return cachedCurrentBaseUrl
    }

    private companion object {
        val SERVER_SELECTION_KEY = stringPreferencesKey("server_selection")
    }
}