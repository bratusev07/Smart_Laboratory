package ru.bratusev.smartlab.feature_settings.models


data class SettingsState(
    val screenName: String = "Settings Screen",
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,

    val oldSettings: UiSettings = UiSettings(),
    val newSettings: UiSettings = UiSettings()
) {
    val languages = UiSettings.Language.entries.map { it.localeName }
    val themes = UiSettings.Theme.entries.map { it.localeName }
    val isChanged = oldSettings != newSettings
}

sealed class Event {
    data class ChangeLanguage(val localeName: String) : Event()
    data class ChangeTheme(val localName: String): Event()
    object Confirm : Event()
}