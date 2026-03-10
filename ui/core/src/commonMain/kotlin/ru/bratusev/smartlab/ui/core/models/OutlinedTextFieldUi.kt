package ru.bratusev.smartlab.ui.core.models

import androidx.compose.ui.graphics.Shape

data class OutlinedTextFieldUi(
    val value: String,
    val placeholder: String,
    val singleLine: Boolean = true,
    val enableHidingPassword: Boolean = false,
    val shape: Shape? = null,
    val onValueChange: (String) -> Unit
)
