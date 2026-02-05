package ru.bratusev.smartlab.domain.core.model

data class ServerSelection(
    val servers: Map<String, String>,
    val currentServerUrl: String?
)