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
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ru.bratusev.smartlab.data.core.model.SocketResponseModel
import ru.bratusev.smartlab.data.core.remote_storage.message.HomeAssistantMessageHandlers
import ru.bratusev.smartlab.data.core.remote_storage.message.HomeAssistantMessageHandlersImpl
import ru.bratusev.smartlab.data.core.remote_storage.message.HomeAssistantMessageSender
import ru.bratusev.smartlab.data.core.remote_storage.message.HomeAssistantMessageSenderImpl
import ru.bratusev.smartlab.domain.core.repository.ServerSelectionRepository
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.coroutines.cancellation.CancellationException

@OptIn(ExperimentalAtomicApi::class)
class HomeAssistantWebSocketClient(
    private val serverSelectionRepository: ServerSelectionRepository
) {

    // Внутренний долгоживущий scope.
    // SupervisorJob гарантирует, что ошибка в одной корутине не сломает весь scope.
    private val clientScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var accessToken: String? = null
    private val client = HttpClient(CIO) {
        install(HttpTimeout) {
            requestTimeoutMillis = 10_000
            socketTimeoutMillis = 30_000
        }
        install(WebSockets)
    }

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    // Потокобезопасный счетчик ID
    private var messageId = AtomicInt(1)

    private var session: DefaultClientWebSocketSession? = null
    private var job: Job? = null

    // Буфер, чтобы не терять события
    private val _socketResponseFlow = MutableSharedFlow<SocketResponseModel>(
        replay = 1,
        extraBufferCapacity = 5,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val socketResponseFlow: SharedFlow<SocketResponseModel> = _socketResponseFlow
    var serviceEntityCopy: SocketResponseModel.DeviceEntity? = null

    private val messageHandlers: HomeAssistantMessageHandlers by lazy {
        HomeAssistantMessageHandlersImpl(
            json = json,
            errorFlow = _socketResponseFlow,
            sessionProvider = { session } // Передаем лямбду для доступа к актуальной сессии
        )
    }

    private val messageSender: HomeAssistantMessageSender by lazy {
        HomeAssistantMessageSenderImpl(
            json = json,
            session = { session },
            messageId = { messageId.load() },
            incrementMessageId = { messageId.addAndFetch(1) },
            errorFlow = _socketResponseFlow
        )
    }

    internal val sender: HomeAssistantMessageSender get() = messageSender

    internal fun setToken(newToken: String): HomeAssistantWebSocketClient {
        accessToken = newToken
        return this
    }

    internal fun connect() {
        // Отменяем предыдущую попытку соединения, если была
        job?.cancel()

        job = clientScope.launch {
            try {
                val rawUrl = serverSelectionRepository.getCurrentBaseUrl() ?: ""
                val wsUrl = rawUrl.replace(Regex("^http"), "ws") + "/api/websocket"

                client.webSocket(wsUrl) {
                    session = this
                    for (frame in incoming) {
                        if (frame is Frame.Text) {
                            handleTextMessage(frame.readText())
                        }
                    }
                }
            } catch (e: CancellationException) {
                throw e // Нормальное закрытие корутины
            } catch (e: Exception) {
                e.printStackTrace()
                _socketResponseFlow.tryEmit(
                    SocketResponseModel.ErrorMessage(listOf(Error("WebSocket error: ${e.message}")))
                )
            } finally {
                session?.close(CloseReason(CloseReason.Codes.NORMAL, "Closed by client"))
                session = null
            }
        }
    }

    internal suspend fun disconnect() {
        job?.cancel() // finally блок внутри connect() сам закроет сессию
    }

    private fun handleTextMessage(text: String) {
        try {
            val jsonElement = json.parseToJsonElement(text)
            val type =
                runCatching { jsonElement.jsonObject["type"]?.jsonPrimitive?.content }.getOrNull()
                    ?: return

            println("type is $type")

            when (type) {
                "auth_required" -> messageHandlers.handleAuthRequired(
                    jsonElement = jsonElement,
                    accessToken = accessToken
                )

                "auth_ok" -> messageHandlers.handleAuthOk(
                    jsonElement = jsonElement,
                    getMessageId = { messageId.load() },
                    setMessageId = { newId -> messageId.store(newId) }
                )

                "auth_invalid" -> messageHandlers.handleAuthInvalid(jsonElement = jsonElement)

                "result" -> messageHandlers.handleResult(
                    jsonElement = jsonElement,
                    emitAreaEntity = { list ->
                        _socketResponseFlow.tryEmit(SocketResponseModel.AreasEntity(list))
                    },
                    emitAreaDevices = { list ->
                        val devices = SocketResponseModel.AreaDeviceEntity(
                            serviceEntityCopy?.services?.filter { list.contains(it.id) }
                                ?: emptyList()
                        )
                        _socketResponseFlow.tryEmit(devices)
                    },
                    collectAutomationUrl = { url ->
                        _socketResponseFlow.tryEmit(SocketResponseModel.AutomationUrl(url))
                    },
                    collectIngressSession = { id ->
                        _socketResponseFlow.tryEmit(SocketResponseModel.IngressSessionId(id))
                    }
                )

                "event" -> messageHandlers.handleEvent(
                    jsonElement = jsonElement,
                    getServiceEntitiesCopy = { serviceEntityCopy?.services ?: emptyList() },
                    setServiceEntitiesCopy = { list ->
                        serviceEntityCopy = SocketResponseModel.DeviceEntity(list)
                    },
                    emitServiceEntities = { list ->
                        _socketResponseFlow.tryEmit(SocketResponseModel.DeviceEntity(list))
                    }
                )

                "pong" -> messageHandlers.handlePong(jsonElement = jsonElement)
                else -> {
                    println("Unknown message type: $type")
                }
            }
        } catch (e: Exception) {
            _socketResponseFlow.tryEmit(
                SocketResponseModel.ErrorMessage(listOf(Error("Parse error: ${e.message}")))
            )
        }
    }
}