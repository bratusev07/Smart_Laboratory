package ru.bratusev.smartlab.ui.core.models

data class OutlinedTextFieldUi(
    val value: String,
    val placeholder: String,
    val singleLine: Boolean = true,
    val isSecret: Boolean = false,
    val onValueChange: (String) -> Unit
)
