package ru.bratusev.smartlab.data.core.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginFlowRequestBody(
    val client_id: String,
    val handler: List<String?>,
    val redirect_uri: String
)

@Serializable
data class LoginFlowRequestBodyWithId(
    val username: String,
    val password: String,
    val client_id: String
)

@Serializable
data class ConfigRequestBody(
    val client_id: String,
    val handler: List<String?>,
    val redirect_uri: String,
    val type: String
)


@Serializable
data class RegistrationRequest(
    val app_id: String,
    val app_name: String,
    val app_version: String,
    val device_name: String,
    val manufacturer: String,
    val model: String,
    val os_name: String,
    val os_version: String,
    val supports_encryption: Boolean,
    val app_data: AppData,
    val device_id: String
)

@Serializable
data class AppData(
    val push_websocket_channel: Boolean,
    val push_url: String,
    val push_token: String
)