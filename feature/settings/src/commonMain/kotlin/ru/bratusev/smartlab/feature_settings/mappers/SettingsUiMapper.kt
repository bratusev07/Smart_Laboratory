package ru.bratusev.smartlab.feature_settings.mappers

import ru.bratusev.smartlab.domain.core.model.Settings
import ru.bratusev.smartlab.feature_settings.models.UiSettings
import ru.bratusev.smartlab.feature_settings.models.UiSettings.Theme.DARK
import ru.bratusev.smartlab.feature_settings.models.UiSettings.Theme.LIGHT
import ru.bratusev.smartlab.feature_settings.models.UiSettings.Theme.SYSTEM

fun UiSettings.toDomain(): Settings = Settings(
    isoLanguage = this.language.isoLanguage, theme = when (this.theme) {
        LIGHT -> Settings.Theme.SYSTEM
        DARK -> Settings.Theme.DARK
        else -> Settings.Theme.SYSTEM
    }
)

fun Settings.toUi(): UiSettings = UiSettings(
    language = UiSettings.Language.entries.find { it.isoLanguage == this.isoLanguage }
        ?: UiSettings.Language.SYSTEM,
    theme = when (this.theme) {
        Settings.Theme.LIGHT -> LIGHT
        Settings.Theme.DARK -> DARK
        else -> SYSTEM
    }
)