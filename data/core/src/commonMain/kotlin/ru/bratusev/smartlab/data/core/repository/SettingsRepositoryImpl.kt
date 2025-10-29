package ru.bratusev.smartlab.data.core.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import ru.bratusev.smartlab.data.core.mapper.toDomain
import ru.bratusev.smartlab.data.core.mapper.toEntity
import ru.bratusev.smartlab.data.core.model.SettingsEntity
import ru.bratusev.smartlab.domain.core.model.Settings
import ru.bratusev.smartlab.domain.core.repository.SettingsRepository

class SettingsRepositoryImpl(private val dataStore: DataStore<Preferences>) : SettingsRepository {
    override suspend fun getSettings(): Settings? {
        val settingsJsonString = dataStore.data.map { preferences ->
            preferences[SETTINGS_KEY]
        }.first() ?: return null
        val result = Json.decodeFromString<SettingsEntity>(settingsJsonString).toDomain()
        return result
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
