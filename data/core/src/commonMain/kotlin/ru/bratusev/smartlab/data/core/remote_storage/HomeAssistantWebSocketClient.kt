package ru.bratusev.smartlab.data.core.remote_storage

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
import ru.bratusev.smartlab.data.core.model.SocketResponseModel
import ru.bratusev.smartlab.data.core.remote_storage.message.HomeAssistantMessageHandlers
import ru.bratusev.smartlab.data.core.remote_storage.message.HomeAssistantMessageHandlersImpl
import ru.bratusev.smartlab.data.core.remote_storage.message.HomeAssistantMessageSender
import ru.bratusev.smartlab.data.core.remote_storage.message.HomeAssistantMessageSenderImpl
import ru.bratusev.smartlab.domain.core.repository.ServerSelectionRepository


class HomeAssistantWebSocketClient(
    private val serverSelectionRepository: ServerSelectionRepository
) {

    private var accessToken: String? = null
    private val client = HttpClient(CIO) {
        install(HttpTimeout) {
            requestTimeoutMillis = 10_000
            socketTimeoutMillis = 30_000
            requestTimeoutMillis = 10_000
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

    private val _socketResponseFlow = MutableSharedFlow<SocketResponseModel>(replay = 1)
    val socketResponseFlow: SharedFlow<SocketResponseModel> = _socketResponseFlow
    var serviceEntityCopy: SocketResponseModel.DeviceEntity? = null

    private val messageHandlers: HomeAssistantMessageHandlers by lazy {
        HomeAssistantMessageHandlersImpl(
            json = json, session = session, errorFlow = _socketResponseFlow
        )
    }
    private val messageSender: HomeAssistantMessageSender by lazy {
        HomeAssistantMessageSenderImpl(
            json = json,
            session = { session },
            messageId = { messageId },
            incrementMessageId = { messageId++ },
            errorFlow = _socketResponseFlow
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
                client.webSocket(
                    (serverSelectionRepository.getCurrentBaseUrl() ?: "").replace(
                        "http", "ws"
                    ) + "/api/websocket"
                ) {
                    session = this
                    for (frame in incoming) {
                        if (frame is Frame.Text) {
                            handleTextMessage(frame.readText())
                        }
                    }
                }
            } catch (e: Exception) {
                // TODO Пофиксить краш сокета на Android
                println("WebSocket error: $e")
                _socketResponseFlow.tryEmit(SocketResponseModel.ErrorMessage(listOf(Error("WebSocket error: ${e.message ?: e.toString()}"))))
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
                    jsonElement = jsonElement, accessToken = accessToken
                )

                "auth_ok" -> messageHandlers.handleAuthOk(
                    jsonElement = jsonElement,
                    getMessageId = { messageId },
                    setMessageId = { newId -> messageId = newId })

                "auth_invalid" -> messageHandlers.handleAuthInvalid(jsonElement = jsonElement)
                "result" -> messageHandlers.handleResult(
                    jsonElement = jsonElement,
                    emitAreaEntity = { list ->
                        _socketResponseFlow.tryEmit(
                            SocketResponseModel.AreasEntity(
                                list
                            )
                        )
                    },
                    emitAreaDevices = { list ->
                        val devices =
                            SocketResponseModel.AreaDeviceEntity(serviceEntityCopy?.services?.filter {
                                list.contains(it.id)
                            } ?: emptyList())
                        _socketResponseFlow.tryEmit(devices)
                    },
                    collectAutomationUrl = { url ->
                        _socketResponseFlow.tryEmit(SocketResponseModel.AutomationUrl(url))
                    },
                    collectIngressSession = { id ->
                        _socketResponseFlow.tryEmit(SocketResponseModel.IngressSessionId(id))
                    })

                "event" -> messageHandlers.handleEvent(
                    jsonElement = jsonElement,
                    getServiceEntitiesCopy = { serviceEntityCopy?.services ?: emptyList() },
                    setServiceEntitiesCopy = { list ->
                        serviceEntityCopy = SocketResponseModel.DeviceEntity(list)
                    },
                    emitServiceEntities = { list ->
                        _socketResponseFlow.tryEmit(
                            SocketResponseModel.DeviceEntity(
                                list
                            )
                        )
                    })

                "pong" -> messageHandlers.handlePong(jsonElement = jsonElement)
                else -> {
                    println("Unknown message type: $type")
                    _socketResponseFlow.tryEmit(SocketResponseModel.ErrorMessage(listOf(Error("Unknown message type: $type"))))
                }
            }
        } catch (e: Exception) {
            println("Parse error: $e")
            _socketResponseFlow.tryEmit(SocketResponseModel.ErrorMessage(listOf(Error("Parse error: ${e.message ?: e.toString()}"))))
        }
    }

}