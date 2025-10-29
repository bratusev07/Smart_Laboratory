package ru.bratusev.smartlab.ui.core.models

import androidx.compose.ui.text.TextStyle

data class CustomButtonUi(
    val title: String,
    val fontWeight: Int? = null,
    val textStyle: TextStyle? = null,
    val isEnabled: Boolean = true
)
