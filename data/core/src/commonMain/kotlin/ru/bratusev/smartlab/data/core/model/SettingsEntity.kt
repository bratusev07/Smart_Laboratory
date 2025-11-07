package ru.bratusev.smartlab.data.core.model

import kotlinx.serialization.Serializable

@Serializable
data class SettingsEntity(
    val theme: String,
    val isoLanguage: String
)
