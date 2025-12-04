package ru.bratusev.smartlab.domain.core.model

data class NetworkStatus(
    val ip: String,
    val baseUrl: String,
    val isVpnActive: Boolean,
)
