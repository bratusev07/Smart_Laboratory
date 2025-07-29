package ru.bratusev.smartlab.data.core.model

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.delay
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class HaMessage(
    val type: String,
    @SerialName("id") val id: Int? = null,
    @SerialName("access_token") val accessToken: String? = null,
    @SerialName("event_type") val eventType: String? = null,
    //@SerialName("event_data") val eventData: Map<String, Any>? = null,
    @SerialName("service") val service: String? = null,
    @SerialName("domain") val domain: String? = null,
    //@SerialName("service_data") val serviceData: Map<String, Any>? = null,
    //@SerialName("target") val target: Map<String, Any>? = null,
    @SerialName("return_response") val returnResponse: Boolean? = null,
    @SerialName("features") val features: Map<String, Int>? = null,
    //@SerialName("trigger") val trigger: Map<String, Any>? = null,
    @SerialName("subscription") val subscription: Int? = null,
    val success: Boolean? = null,
    val result: JsonElement? = null,
    val error: HaError? = null,
    val event: HaEvent? = null
)

@Serializable
data class HaError(
    val code: String,
    val message: String
)

@Serializable
data class HaEvent(
    val event_type: String,
    //val data: Map<String, Any>? = null,
    val time_fired: String,
    val origin: String,
    val context: Map<String, String?>? = null
)

suspend fun connectSocket(token: String) {
    val client = HttpClient(CIO) {
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json {
                ignoreUnknownKeys = true
                explicitNulls = false
            })
        }
    }

    val host = "http://10.131.170.254:8123"
    val wsUrl = "ws://$host/api/websocket"

    client.webSocketSession(wsUrl).let { session ->
        println("Соединение установлено")

        var authenticated = false
        var nextId = 1

        while (true) {
            val frame = try {
                session.incoming.receive()
            } catch (e: Exception) {
                println("Соединение закрыто: ${e.message}")
                break
            }

            if (frame is Frame.Text) {
                val text = frame.readText()
                println("Получено: $text")

                val json = Json.decodeFromString<JsonObject>(text)
                val type = json["type"]?.jsonPrimitive?.content ?: continue

                when {
                    type == "auth_required" -> {
                        // Шаг 1: Отправляем токен
                        val authMsg = HaMessage(type = "auth", accessToken = token)
                        session.send(Frame.Text(Json.encodeToString(authMsg)))
                        println("Отправлено: $authMsg")
                    }

                    type == "auth_ok" -> {
                        println("✅ Аутентификация успешна")
                        authenticated = true

                        // После аутентификации — можно отправлять команды
                        // Например: включить фичу coalesce_messages
                        val featuresMsg = HaMessage(
                            id = nextId++,
                            type = "supported_features",
                            features = mapOf("coalesce_messages" to 1)
                        )
                        session.send(Frame.Text(Json.encodeToString(featuresMsg)))
                        println("Отправлено: $featuresMsg")

                        // Подписываемся на события изменения состояния
                        val subscribeMsg = HaMessage(
                            id = nextId++,
                            type = "subscribe_events",
                            eventType = "state_changed"
                        )
                        session.send(Frame.Text(Json.encodeToString(subscribeMsg)))
                        println("Подписались на state_changed")

                        // Вызов сервиса: включить свет (пример)
                        val callServiceMsg = HaMessage(
                            id = nextId++,
                            type = "call_service",
                            domain = "light",
                            service = "turn_on",
                            //serviceData = mapOf("brightness" to 100),
                            //target = mapOf("entity_id" to "light.bed_light"),
                            returnResponse = true
                        )
                        session.send(Frame.Text(Json.encodeToString(callServiceMsg)))
                        println("Вызван сервис: light.turn_on")

                        // Пинг для проверки соединения
                        delay(10_000)
                        val pingMsg = HaMessage(id = nextId++, type = "ping")
                        session.send(Frame.Text(Json.encodeToString(pingMsg)))
                    }

                    type == "auth_invalid" -> {
                        println("❌ Ошибка аутентификации: ${json["message"]}")
                        session.close()
                        break
                    }

                    type == "result" -> {
                        val id = json["id"]?.jsonPrimitive?.intOrNull
                        val success = json["success"]?.jsonPrimitive?.booleanOrNull ?: false
                        val errorMsg = json["error"]?.jsonObject

                        if (success) {
                            println("✅ Результат команды $id: Успешно")
                            if (json["result"] != null) {
                                println("Данные: ${json["result"]}")
                            }
                        } else {
                            val error = errorMsg?.let {
                                Json.decodeFromJsonElement<HaError>(it)
                            }
                            println("❌ Ошибка в команде $id: ${error?.code} — ${error?.message}")
                        }
                    }

                    type == "event" -> {
                        val eventId = json["id"]?.jsonPrimitive?.intOrNull
                        val haEvent = Json.decodeFromJsonElement<HaEvent>(json["event"]!!)
                        println("🔔 Событие $eventId: ${haEvent.event_type} | Данные: $")
                    }

                    type == "pong" -> {
                        println("🏓 Получен pong")
                    }

                    else -> {
                        println("⚠️ Неизвестный тип сообщения: $type")
                    }
                }
            }
        }
    }
}