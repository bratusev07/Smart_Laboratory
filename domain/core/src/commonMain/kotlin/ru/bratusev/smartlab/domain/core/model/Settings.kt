package ru.bratusev.smartlab.domain.core.model

data class Settings(
    val isoLanguage: String, // в iso https://en.wikipedia.org/wiki/List_of_ISO_639_language_codes
    // Системный будет system
    val theme: Theme
){
    enum class Theme {
        SYSTEM,
        LIGHT,
        DARK
    }
}
