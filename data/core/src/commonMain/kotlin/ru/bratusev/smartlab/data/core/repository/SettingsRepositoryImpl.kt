package ru.bratusev.smartlab.data.core.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import ru.bratusev.smartlab.data.core.mapper.toDomain
import ru.bratusev.smartlab.data.core.mapper.toEntity
import ru.bratusev.smartlab.data.core.model.SettingsEntity
import ru.bratusev.smartlab.domain.core.model.Settings
import ru.bratusev.smartlab.domain.core.repository.SettingsRepository

class SettingsRepositoryImpl(private val dataStore: DataStore<Preferences>) : SettingsRepository {
    override fun observeSettings(): Flow<Settings?> {
        return dataStore.data.map { preferences ->
            val settingsJsonString = preferences[SETTINGS_KEY]
            if (settingsJsonString != null) {
                Json.decodeFromString<SettingsEntity>(settingsJsonString).toDomain()
            } else {
                null
            }
        }
    }

    override suspend fun updateSettings(newSettings: Settings) {
        val jsonString = Json.encodeToString(newSettings.toEntity())
        dataStore.edit { preferences ->
            preferences[SETTINGS_KEY] = jsonString
        }
    }

    private companion object {
        val SETTINGS_KEY = stringPreferencesKey("settings")
    }
}
