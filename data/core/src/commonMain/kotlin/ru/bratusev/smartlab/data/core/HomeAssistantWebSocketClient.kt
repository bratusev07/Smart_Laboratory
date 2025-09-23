package ru.bratusev.smartlab.data.core

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ru.bratusev.smartlab.data.core.message.HomeAssistantMessageHandlers
import ru.bratusev.smartlab.data.core.message.HomeAssistantMessageHandlersImpl
import ru.bratusev.smartlab.data.core.message.HomeAssistantMessageSender
import ru.bratusev.smartlab.data.core.message.HomeAssistantMessageSenderImpl
import ru.bratusev.smartlab.data.core.model.AreaEntity
import ru.bratusev.smartlab.data.core.model.ServiceEntity


class HomeAssistantWebSocketClient() {

    private var accessToken: String? = null

    private val client = HttpClient(CIO) {
        install(HttpTimeout) {
            requestTimeoutMillis = 10000
        }
        install(WebSockets)
    }

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
    private var messageId = 1

    private var session: DefaultClientWebSocketSession? = null
    private var job: Job? = null

    private val _socketErrorsFlow = MutableSharedFlow<List<Error>>()
    val socketErrorsFlow: SharedFlow<List<Error>> = _socketErrorsFlow

    private val _serviceEntitiesFlow = MutableSharedFlow<List<ServiceEntity>>(replay = 1)
    internal var _serviceEntityCopy: List<ServiceEntity> = emptyList()
    val serviceEntitiesFlow: SharedFlow<List<ServiceEntity>> = _serviceEntitiesFlow

    private val _areasFlow = MutableSharedFlow<List<AreaEntity>>(replay = 1)
    val areasFlow: SharedFlow<List<AreaEntity>> = _areasFlow

    private val messageHandlers: HomeAssistantMessageHandlers by lazy {
        HomeAssistantMessageHandlersImpl(
            json = json,
            session = session,
            errorFlow = _socketErrorsFlow
        )
    }

    private val messageSender: HomeAssistantMessageSender by lazy {
        HomeAssistantMessageSenderImpl(
            json = json,
            session = { session },
            messageId = { messageId },
            incrementMessageId = { messageId++ },
            errorFlow = _socketErrorsFlow
        )
    }

    internal val sender: HomeAssistantMessageSender get() = messageSender

    internal fun setToken(newToken: String): HomeAssistantWebSocketClient {
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
                _socketErrorsFlow.tryEmit(listOf(Error("WebSocket error: ${e.message ?: e.toString()}")))
            } finally {
                disconnect()
                session = null
            }
        }
    }

    private suspend fun disconnect() {
        job?.cancel()
        session?.close(CloseReason(CloseReason.Codes.NORMAL, "Closed by client"))
    }

    private fun handleTextMessage(text: String) {
        try {
            val jsonElement = json.parseToJsonElement(text)
            val type = jsonElement.jsonObject["type"]?.jsonPrimitive?.content ?: return
            println("type is $type")

            when (type) {
                "auth_required" -> messageHandlers.handleAuthRequired(
                    jsonElement = jsonElement,
                    accessToken = accessToken
                )

                "auth_ok" -> messageHandlers.handleAuthOk(
                    jsonElement = jsonElement,
                    getMessageId = { messageId },
                    setMessageId = { newId -> messageId = newId }
                )

                "auth_invalid" -> messageHandlers.handleAuthInvalid(jsonElement = jsonElement)
                "result" -> messageHandlers.handleResult(
                    jsonElement = jsonElement,
                    emitAreaEntity = { list -> _areasFlow.tryEmit(list)}
                )
                "event" -> messageHandlers.handleEvent(
                    jsonElement = jsonElement,
                    getServiceEntitiesCopy = { _serviceEntityCopy },
                    setServiceEntitiesCopy = { list -> _serviceEntityCopy = list },
                    emitServiceEntities = { list -> _serviceEntitiesFlow.tryEmit(list) }
                )

                "pong" -> messageHandlers.handlePong(jsonElement = jsonElement)
                else -> {
                    println("Unknown message type: $type")
                    _socketErrorsFlow.tryEmit(listOf(Error("Unknown message type: $type")))
                }
            }
        } catch (e: Exception) {
            println("Parse error: $e")
            _socketErrorsFlow.tryEmit(listOf(Error("Parse error: ${e.message ?: e.toString()}")))
        }
    }

}