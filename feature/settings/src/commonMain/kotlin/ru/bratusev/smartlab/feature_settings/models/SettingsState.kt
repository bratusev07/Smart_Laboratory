package ru.bratusev.smartlab.feature_settings.models

import org.jetbrains.compose.resources.StringResource


data class SettingsState(
    val screenName: String = "Settings Screen",
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,

    val oldSettings: UiSettings = UiSettings(),
    val newSettings: UiSettings = UiSettings()
) {
    val languages = UiSettings.Language.entries.map { it.localNameRes}
    val themes = UiSettings.Theme.entries.map { it.localeNameRes }
    val isChanged = oldSettings != newSettings
}

sealed class Event {
    data class ChangeLanguage(val localeName: StringResource) : Event()
    data class ChangeTheme(val localName: StringResource): Event()
    object OnTimeOut: Event()
    object Confirm : Event()
}