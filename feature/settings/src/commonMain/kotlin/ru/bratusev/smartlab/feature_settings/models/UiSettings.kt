package ru.bratusev.smartlab.feature_settings.models

import org.jetbrains.compose.resources.StringResource
import smartlaboratory.ui.core.generated.resources.Res
import smartlaboratory.ui.core.generated.resources.language_english
import smartlaboratory.ui.core.generated.resources.language_russian
import smartlaboratory.ui.core.generated.resources.language_system
import smartlaboratory.ui.core.generated.resources.theme_dark
import smartlaboratory.ui.core.generated.resources.theme_light
import smartlaboratory.ui.core.generated.resources.theme_system

data class UiSettings(
    val language: Language = Language.SYSTEM, val theme: Theme = Theme.SYSTEM
) {
    enum class Theme(val localeNameRes: StringResource) {
        SYSTEM(Res.string.theme_system), LIGHT(Res.string.theme_light), DARK(Res.string.theme_dark);

        companion object {
            fun fromLocaleNameRes(localeNameRes: StringResource): Theme =
                Theme.entries.find { it.localeNameRes == localeNameRes } ?: SYSTEM
        }
    }

    enum class Language(
        val localNameRes: StringResource,
        val isoLanguage: String,
    ) {
        SYSTEM(Res.string.language_system, "system"), RU(
            Res.string.language_russian,
            "ru"
        ),
        EN(Res.string.language_english, "en");

        companion object {
            fun fromLocaleNameRes(localeNameRes: StringResource): Language =
                entries.find { it.localNameRes == localeNameRes } ?: SYSTEM
        }
    }
}
