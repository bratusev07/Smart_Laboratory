package ru.bratusev.smartlab.domain.core.repository

import kotlinx.coroutines.flow.Flow
import ru.bratusev.smartlab.domain.core.model.Settings

interface SettingsRepository {
    fun observeSettings(): Flow<Settings?>
    suspend fun updateSettings(newSettings: Settings)
}