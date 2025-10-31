package ru.bratusev.smartlab

import ru.bratusev.smartlab.domain.core.model.Settings
import ru.bratusev.smartlab.feature_settings.models.UiSettings
import ru.bratusev.smartlab.feature_settings.models.UiSettings.Language
import ru.bratusev.smartlab.feature_settings.models.UiSettings.Theme

fun Settings.toUi(): UiSettings = UiSettings(
    language = Language.entries.find { it.isoLanguage == this.isoLanguage }
        ?: Language.SYSTEM,
    theme = when (this.theme) {
        Settings.Theme.LIGHT -> Theme.LIGHT
        Settings.Theme.DARK -> Theme.DARK
        else -> Theme.SYSTEM
    }
)
