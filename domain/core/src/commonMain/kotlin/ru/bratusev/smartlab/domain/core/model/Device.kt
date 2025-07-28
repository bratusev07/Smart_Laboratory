package ru.bratusev.smartlab.domain.core.model

data class Device (
    val appId: String,
    val appName: String,
    val appVersion: String,
    val deviceName: String,
    val manufacturer: String,
    val model: String,
    val osName: String,
    val osVersion: String,
    val deviceId: String
)