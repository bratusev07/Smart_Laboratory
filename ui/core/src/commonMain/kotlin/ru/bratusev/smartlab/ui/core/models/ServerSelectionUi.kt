package ru.bratusev.smartlab.ui.core.models

data class ServerSelectionUi(
    val serverList: Map<String, String>,
    val currentServerUrl: String,
    val expanded: Boolean
)
