package ru.bratusev.smartlab.data.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
sealed class WebSocketResponse {
    abstract val type: String
    abstract val id: Int?
}

@Serializable
data class PongResponseMsg(override val id: Int) : WebSocketResponse() {
    override val type: String = "pong"
}

@Serializable
data class TriggerResponseMsg(
    override val id: Int,
    val event: TriggerEventData,
) : WebSocketResponse() {
    override val type: String = "event"
}

@Serializable
data class EventResponseMsg(
    override val id: Int,
    val event: EventData,
) : WebSocketResponse() {
    override val type: String = "event"
}

@Serializable
data class AuthRequiredResponseMsg(
    @SerialName("ha_version") val haVersion: String? = null,
) : WebSocketResponse() {
    override val type: String = "auth_required"
    override val id: Int? = null
}

@Serializable
data class AuthOkResponseMsg(
    @SerialName("ha_version") val haVersion: String,
) : WebSocketResponse() {
    override val type: String = "auth_ok"
    override val id: Int? = null
}

@Serializable
data class AuthInvalidResponseMsg(val message: String) : WebSocketResponse() {
    override val type: String = "auth_invalid"
    override val id: Int? = null
}

@Serializable
data class ResultResponseMsg(
    override val id: Int,
    val success: Boolean,
    val result: JsonElement? = null,
    val error: ApiError? = null,
) : WebSocketResponse() {
    override val type: String = "result"
}

@Serializable
data class ApiError(
    val code: String,
    val message: String,
    val translation_key: String? = null,
    val translation_domain: String? = null,
    val translation_placeholders: Map<String, String>? = null,
)

@Serializable
data class EventData(
    @SerialName("event_type") val eventType: String,
    val data: Map<String, JsonElement>? = null,
    val origin: String? = null,
    @SerialName("time_fired") val timeFired: String,
    val context: Context? = null,
)

@Serializable
data class TriggerEventData(
    val variables: Map<String, JsonElement>,
    val description: String,
)

@Serializable
data class Context(
    val id: String? = null,
    @SerialName("parent_id") val parentId: String? = null,
    @SerialName("user_id") val userId: String? = null,
)

@Serializable
data class ServiceData(
    @SerialName("entity_id") val entityId: String,
)

@Serializable
data class ServiceEntity(
    @SerialName("s") val state: JsonElement? = null,
    @SerialName("a") val rawAttributes: JsonElement? = null,
    val attributes: ServiceEntityAttributes? = null,
    val c: JsonElement? = null,
    val id: String? = null,
    val domain: String? = null,
    @SerialName("lu") val lastUpdate: String? = null,
    @SerialName("lc") val lastChange: String? = null,
)

@Serializable
data class ServiceEntityAttributes(
    val icon: String? = null,
    @SerialName("friendly_name") val friendlyName: String? = null,
    @SerialName("device_class") val deviceClass: String? = null,
    @SerialName("unit_of_measurement") val measurementUnit: String? = null
)

// === WebSocket Messages ===

@Serializable
sealed class SocketMessage {

    @Serializable
    data class AuthMsg(
        @SerialName("type") val type: String = "auth",
        @SerialName("access_token") val accessToken: String,
    ) : SocketMessage() {

        override fun toString(): String {
            return """{"type": "$type", "access_token" : "$accessToken"}"""
        }
    }

    @Serializable
    data class PingMsg(val type: String = "ping", val id: Int) : SocketMessage()

    @Serializable
    data class SubEntitiesMsg(
        val type: String = "subscribe_entities",
        val id: Int,
    ) : SocketMessage() {

        override fun toString(): String {
            return """{"type": "$type", "id" : $id}"""
        }
    }

    @Serializable
    data class SensorMsg(
        val type: String = "call_service",
        val domain: String,
        val service: String,
        @SerialName("return_response") val returnResponse: Boolean = false,
        @SerialName("service_data") val serviceData: ServiceData,
        val id: Int,
    ) : SocketMessage()
}