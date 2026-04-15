package ru.bratusev.smartlab.domain.core.model

data class ServerSelection(
    // url, name, login, password
    val servers: List<List<String>>,
    val currentServerUrl: String?,
    val currentServerName: String?
)