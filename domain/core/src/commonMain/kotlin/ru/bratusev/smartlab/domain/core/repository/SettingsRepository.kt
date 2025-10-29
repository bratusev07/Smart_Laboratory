package ru.bratusev.smartlab.domain.core.repository

import ru.bratusev.smartlab.domain.core.model.Settings

interface SettingsRepository {
    suspend fun getSettings(): Settings?
    suspend fun updateSettings(newSettings: Settings)
}