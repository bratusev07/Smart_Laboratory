package ru.bratusev.smartlab.data.core

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ru.bratusev.smartlab.data.core.model.ApiError
import ru.bratusev.smartlab.data.core.model.AuthInvalidResponseMsg
import ru.bratusev.smartlab.data.core.model.AuthOkResponseMsg
import ru.bratusev.smartlab.data.core.model.AuthRequiredResponseMsg
import ru.bratusev.smartlab.data.core.model.EventResponseMsg
import ru.bratusev.smartlab.data.core.model.ResultResponseMsg
import ru.bratusev.smartlab.data.core.model.SocketMessage
import ru.bratusev.smartlab.data.core.model.TriggerResponseMsg


class HomeAssistantWebSocketClient() {

    private var accessToken: String? = null

    private val client = HttpClient(CIO) {
        install(WebSockets)
    }

    private val json = Json { ignoreUnknownKeys = true }
    private val messageId = 1

    private var session: DefaultClientWebSocketSession? = null
    private var job: Job? = null

    fun setToken(newToken: String): HomeAssistantWebSocketClient {
        accessToken = newToken
        return this
    }

    internal suspend fun connect() = withContext(Dispatchers.IO) {
        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                client.webSocket(Constants.BASE_URL.replace("http", "ws") + "/api/websocket") {
                    session = this
                    for (frame in incoming) {
                        if (frame is Frame.Text) {
                            handleTextMessage(frame.readText())
                        }
                    }
                }
            } catch (e: Exception) {
                println("WebSocket error: $e")
            } finally {
                session = null
            }
        }
    }

    suspend fun disconnect() {
        job?.cancel()
        session?.close(CloseReason(CloseReason.Codes.NORMAL, "Closed by client"))
    }

    private suspend fun send(msg: SocketMessage) {
        session?.send(Frame.Text(msg.toString()))
    }

    private fun handleTextMessage(text: String) {
        try {
            val jsonElement = json.parseToJsonElement(text)
            val type = jsonElement.jsonObject["type"]?.jsonPrimitive?.content ?: return
            when (type) {
                "auth_required" -> handleAuthRequired(jsonElement)
                "auth_ok" -> handleAuthOk(jsonElement)
                "auth_invalid" -> handleAuthInvalid(jsonElement)
                "result" -> handleResult(jsonElement)
                "event" -> handleEvent(jsonElement)
                "pong" -> handlePong(jsonElement)
                else -> println("Unknown message type: $type")
            }
        } catch (e: Exception) {
            println("Parse error: $e")
        }
    }

    private fun handleEvent(jsonElement: JsonElement) {
        val eventObj = jsonElement.jsonObject
        if (eventObj["event"]?.jsonObject?.get("event_type") != null) {
            val eventMsg = json.decodeFromJsonElement<EventResponseMsg>(jsonElement)
        } else if (eventObj["event"]?.jsonObject?.get("variables") != null) {
            val triggerMsg = json.decodeFromJsonElement<TriggerResponseMsg>(jsonElement)
        }
        parseToServiceEntityMap(jsonElement.toString())
    }

    private fun handleResult(jsonElement: JsonElement) {
        val id = jsonElement.jsonObject["id"]?.jsonPrimitive?.intOrNull ?: return
        val success = jsonElement.jsonObject["success"]?.jsonPrimitive?.booleanOrNull ?: false
        val error = jsonElement.jsonObject["error"]?.let {
            json.decodeFromJsonElement<ApiError>(it)
        }
        val result = jsonElement.jsonObject["result"]
        val resultResponseMsg = ResultResponseMsg(id, success, result, error)
        if (!success) {
            println("Command failed [${resultResponseMsg.id}]: ${resultResponseMsg.error?.message}")
        }
    }

    private fun handleAuthRequired(jsonElement: JsonElement) {
        val msg = json.decodeFromJsonElement<AuthRequiredResponseMsg>(jsonElement)
        println("Auth required. HA version: ${msg.haVersion}")
        CoroutineScope(Dispatchers.IO).launch {
            accessToken?.let {
                session?.send(Frame.Text(SocketMessage.AuthMsg(type = "auth", accessToken = it).toString()))
            }
        }
    }

    private fun handleAuthOk(jsonElement: JsonElement) {
        val msg = json.decodeFromJsonElement<AuthOkResponseMsg>(jsonElement)
        println("Authenticated successfully. HA version: ${msg.haVersion}")
        CoroutineScope(Dispatchers.IO).launch {
            session?.send(Frame.Text(SocketMessage.SubEntitiesMsg(type = "subscribe_entities", id = messageId.getAndIncrement()).toString()))
        }
    }

    private fun handleAuthInvalid(jsonElement: JsonElement) {
        val msg = json.decodeFromJsonElement<AuthInvalidResponseMsg>(jsonElement)
        println("Auth failed: ${msg.message}")
    }

    private fun handlePong(jsonElement: JsonElement) {
        val id = jsonElement.jsonObject["id"]?.jsonPrimitive?.intOrNull ?: return
        println("Pong received: $id")
    }

}