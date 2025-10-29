package ru.bratusev.smartlab.domain.core.usecase

import ru.bratusev.smartlab.domain.core.model.Settings
import ru.bratusev.smartlab.domain.core.repository.SettingsRepository

class GetSettingsUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): Settings? = settingsRepository.getSettings()
}

