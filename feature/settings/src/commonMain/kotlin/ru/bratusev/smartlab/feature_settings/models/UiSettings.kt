package ru.bratusev.smartlab.feature_settings.models

data class UiSettings(
    val language: Language = Language.SYSTEM,
    val theme: Theme = Theme.SYSTEM
) {
    enum class Theme(name: String) {
        SYSTEM("Системная"),
        LIGHT("Светлая"),
        DARK("Тёмная")
    }

    enum class Language(val localeName: String, val isoLanguage: String) {
        SYSTEM("Системный", "system"),
        RU("Русский", "ru"),
        EN("English", "en");

        companion object {
            fun fromLocaleName(localeName: String): Language =
                entries.find { it.localeName == localeName } ?: SYSTEM
        }
    }
}
