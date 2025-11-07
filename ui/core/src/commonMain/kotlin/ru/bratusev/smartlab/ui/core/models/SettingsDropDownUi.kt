package ru.bratusev.smartlab.ui.core.models

import org.jetbrains.compose.resources.StringResource

data class SettingsDropDownUi(
    val label: String,
    val values: List<StringResource>,
    val currentValue: StringResource,
    val originalValue: StringResource
)
