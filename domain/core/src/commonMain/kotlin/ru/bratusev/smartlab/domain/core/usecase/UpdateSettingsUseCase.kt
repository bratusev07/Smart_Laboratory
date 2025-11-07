package ru.bratusev.smartlab.domain.core.usecase

import ru.bratusev.smartlab.domain.core.model.Settings
import ru.bratusev.smartlab.domain.core.repository.SettingsRepository

class UpdateSettingsUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(newSettings: Settings) {
        settingsRepository.updateSettings(newSettings)
    }
}

