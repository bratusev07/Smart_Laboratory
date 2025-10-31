package ru.bratusev.smartlab.domain.core.usecase

import kotlinx.coroutines.flow.Flow
import ru.bratusev.smartlab.domain.core.model.Settings
import ru.bratusev.smartlab.domain.core.repository.SettingsRepository

class GetSettingsUseCase(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<Settings?> = settingsRepository.observeSettings()
}

