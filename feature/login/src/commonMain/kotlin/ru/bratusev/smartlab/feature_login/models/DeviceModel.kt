package ru.bratusev.smartlab.feature_login.models

import org.koin.core.module.Module

expect class Device {
    val appId: String
    val appName: String
    val appVersion: String
    val deviceName: String
    val manufacturer: String
    val model: String
    val osName: String
    val osVersion: String
    val deviceId: String
}

expect val platformLoginModule: Module
expect val platformLoginModulePreview: Module