package ru.bratusev.smartlab.ui.core.models

data class SettingsDropDownUi(
    val label: String,
    val values: List<String>,
    val currentValue: String,
    val originalValue: String
)
