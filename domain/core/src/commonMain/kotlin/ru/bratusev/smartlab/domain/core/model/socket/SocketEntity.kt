package ru.bratusev.smartlab.domain.core.model.socket

data class ServiceEntity(
    val state: String? = null,
    val attributes: ServiceEntityAttributes? = null,
    val c: String? = null,
    val id: String? = null,
    val domain: String? = null,
    val lastUpdate: String? = null,
    val lastChange: String? = null,
)

data class ServiceEntityAttributes(
    val icon: String? = null,
    val friendlyName: String? = null,
    val deviceClass: String? = null,
)