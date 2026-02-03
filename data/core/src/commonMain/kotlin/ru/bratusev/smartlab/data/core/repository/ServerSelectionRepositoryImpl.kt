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
    private val dataStore: DataStore<Preferences>
) : ServerSelectionRepository {
    var isCachedBaseUrlUpToDate: Boolean = false
    var cachedCurrentBaseUrl: String? = null

    override fun observerServerSelection(): Flow<ServerSelection> {
        return dataStore.data.map { preferences ->
            preferences[SERVER_SELECTION_KEY] ?: ""
        }.map { jsonString ->
            try {
                val serverSelection = Json.decodeFromString<ServerSelectionEntity>(jsonString)
                serverSelection.toDomain()
            } catch (e: Exception) {
                ServerSelection(emptyMap(), "")
            }
        }
    }

    override suspend fun setServerSelection(serverSelection: ServerSelection) {
        isCachedBaseUrlUpToDate = false
        val jsonString = Json.encodeToString<ServerSelectionEntity>(
            ServerSelectionEntity.fromDomain(serverSelection)
        )
        dataStore.edit { preferences ->
            preferences[SERVER_SELECTION_KEY] = jsonString
        }
    }

    override fun getCurrentBaseUrl(): String? {
        if (isCachedBaseUrlUpToDate) {
            return cachedCurrentBaseUrl
        }
        cachedCurrentBaseUrl = runBlocking {
            val jsonString = dataStore.data.map { preferences ->
                preferences[SERVER_SELECTION_KEY] ?: ""
            }.first()
            try {
                val serverSelection = Json.decodeFromString<ServerSelectionEntity>(jsonString)
                serverSelection.currentServerUrl
            } catch (e: Exception) {
                null
            }
        }
        isCachedBaseUrlUpToDate = true
        return cachedCurrentBaseUrl
    }

    private companion object {
        val SERVER_SELECTION_KEY = stringPreferencesKey("server_selection")
    }
}