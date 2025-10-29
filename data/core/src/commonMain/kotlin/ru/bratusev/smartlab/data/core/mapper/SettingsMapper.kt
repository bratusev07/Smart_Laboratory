package ru.bratusev.smartlab.data.core.mapper

import ru.bratusev.smartlab.data.core.model.SettingsEntity
import ru.bratusev.smartlab.domain.core.model.Settings

internal fun SettingsEntity.toDomain(): Settings = Settings(
    isoLanguage = this.isoLanguage,
    theme = runCatching { Settings.Theme.valueOf(this.theme) }.getOrDefault(Settings.Theme.SYSTEM)
)

internal fun Settings.toEntity(): SettingsEntity = SettingsEntity(
    theme = this.theme.name,
    isoLanguage = this.isoLanguage
)