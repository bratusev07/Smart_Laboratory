package ru.bratusev.smartlab.data.core.remote_storage.message

import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.websocket.Frame
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ru.bratusev.smartlab.data.core.mapper.mapJsonToEventPair
import ru.bratusev.smartlab.data.core.mapper.mapJsonToServiceEntityList
import ru.bratusev.smartlab.data.core.model.ApiError
import ru.bratusev.smartlab.data.core.model.AreaEntity
import ru.bratusev.smartlab.data.core.model.AuthInvalidResponseMsg
import ru.bratusev.smartlab.data.core.model.AuthOkResponseMsg
import ru.bratusev.smartlab.data.core.model.AuthRequiredResponseMsg
import ru.bratusev.smartlab.data.core.model.EventResponseMsg
import ru.bratusev.smartlab.data.core.model.ServiceEntity
import ru.bratusev.smartlab.data.core.model.SocketMessage
import ru.bratusev.smartlab.data.core.model.SocketResponseModel
import ru.bratusev.smartlab.data.core.model.TriggerResponseMsg

class HomeAssistantMessageHandlersImpl(
    private val json: Json,
    private val errorFlow: MutableSharedFlow<SocketResponseModel>,
    private val sessionProvider: () -> DefaultClientWebSocketSession?
) : HomeAssistantMessageHandlers {

    // Внутренний долгоживущий scope для фоновой отправки сообщений (auth, sub_entities).
    private val handlerScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private fun emitError(message: String) {
        errorFlow.tryEmit(SocketResponseModel.ErrorMessage(listOf(Error(message))))
    }

    override fun handleAuthRequired(jsonElement: JsonElement, accessToken: String?) {
        try {
            val msg = json.decodeFromJsonElement<AuthRequiredResponseMsg>(jsonElement)
            println("Auth required. HA version: ${msg.haVersion}")

            if (accessToken == null) {
                emitError("Access token is null during auth_required")
                return
            }

            handlerScope.launch {
                try {
                    sessionProvider()?.send(
                        Frame.Text(
                            SocketMessage.AuthMsg(type = "auth", accessToken = accessToken).toString()
                        )
                    )
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    emitError("Failed to send auth message: ${e.message}")
                }
            }
        } catch (e: Exception) {
            emitError("AuthRequired handler error: ${e.message}")
        }
    }

    override fun handleAuthOk(jsonElement: JsonElement, getMessageId: () -> Int, setMessageId: (Int) -> Unit) {
        try {
            val msg = json.decodeFromJsonElement<AuthOkResponseMsg>(jsonElement)
            println("Authenticated successfully. HA version: ${msg.haVersion}")

            val newId = getMessageId() + 1
            setMessageId(newId)

            handlerScope.launch {
                try {
                    sessionProvider()?.send(
                        Frame.Text(
                            SocketMessage.SubEntitiesMsg(
                                type = "subscribe_entities",
                                id = newId
                            ).toString()
                        )
                    )
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    emitError("Failed to send subscribe_entities: ${e.message}")
                }
            }
        } catch (e: Exception) {
            emitError("AuthOk handler error: ${e.message}")
        }
    }

    override fun handleAuthInvalid(jsonElement: JsonElement) {
        try {
            val msg = json.decodeFromJsonElement<AuthInvalidResponseMsg>(jsonElement)
            println("Auth failed: ${msg.message}")
            emitError("Auth failed: ${msg.message}")
        } catch (e: Exception) {
            emitError("AuthInvalid handler error: ${e.message}")
        }
    }

    override fun handleResult(
        jsonElement: JsonElement,
        emitAreaEntity: (List<AreaEntity>) -> Boolean,
        emitAreaDevices: (List<String>) -> Unit,
        collectAutomationUrl: (String) -> Unit,
        collectIngressSession: (String) -> Unit
    ) {
        try {
            val id = jsonElement.jsonObject["id"]?.jsonPrimitive?.intOrNull ?: return
            val success = jsonElement.jsonObject["success"]?.jsonPrimitive?.booleanOrNull == true
            val error = jsonElement.jsonObject["error"]?.let {
                json.decodeFromJsonElement<ApiError>(it)
            }
            val result = jsonElement.jsonObject["result"]

            if (!success) {
                val message = "Command failed [$id]: ${error?.message}"
                println(message)
                emitError(message)
                return
            }

            if (result == null) return

            when (result) {
                is JsonArray -> {
                    val firstElement = result.firstOrNull()?.jsonObject
                    if (firstElement?.containsKey("area_id") == true) {
                        try {
                            val areaEntities = result.map { json.decodeFromJsonElement<AreaEntity>(it) }
                            emitAreaEntity(areaEntities)
                        } catch (e: Exception) {
                            emitError("Failed to decode AreaEntity list: ${e.message}")
                        }
                    }
                }
                is JsonObject -> {
                    try {
                        when {
                            result.containsKey("ingress_entry") -> {
                                collectAutomationUrl(result["ingress_entry"]!!.jsonPrimitive.content)
                            }
                            result.containsKey("session") -> {
                                collectIngressSession(result["session"]!!.jsonPrimitive.content)
                            }
                            result.containsKey("entity") -> {
                                val entityObj = result["entity"]
                                if (entityObj != null) {
                                    emitAreaDevices(json.decodeFromJsonElement(entityObj))
                                }
                            }
                        }
                    } catch (e: Exception) {
                        println(e.printStackTrace())
                        emitError("Failed to decode Result object: ${e.message}")
                    }
                }

                else -> {
                    println("АААА не знаю")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emitError("Result handler error: ${e.message}")
        }
    }

    override fun handleEvent(
        jsonElement: JsonElement,
        getServiceEntitiesCopy: () -> List<ServiceEntity>,
        setServiceEntitiesCopy: (List<ServiceEntity>) -> Unit,
        emitServiceEntities: (List<ServiceEntity>) -> Boolean
    ) {
        try {
            val eventObj = jsonElement.jsonObject["event"]?.jsonObject ?: return

            when {
                eventObj.containsKey("event_type") -> {
                    val eventMsg = json.decodeFromJsonElement<EventResponseMsg>(jsonElement)
                    println(eventMsg)
                }
                eventObj.containsKey("variables") -> {
                    val triggerMsg = json.decodeFromJsonElement<TriggerResponseMsg>(jsonElement)
                    println(triggerMsg)
                }
                eventObj.containsKey("a") -> {
                    val list = mapJsonToServiceEntityList(jsonElement.toString())
                    if (emitServiceEntities(list)) {
                        setServiceEntitiesCopy(list)
                    }
                }
                eventObj.containsKey("c") -> {
                    val pair = mapJsonToEventPair(jsonElement.toString())
                    if (pair.second == "null"){
                        return
                    }
                    val updatedServiceEntities = getServiceEntitiesCopy().map {
                        if (it.id == pair.first) {
                            it.copy(state = json.parseToJsonElement(pair.second))
                        } else it
                    }


                    if (emitServiceEntities(updatedServiceEntities)) {
                        setServiceEntitiesCopy(updatedServiceEntities)
                    }
                }
            }
        } catch (e: Exception) {
            emitError("Event handler error: ${e.message}")
        }
    }

    override fun handlePong(jsonElement: JsonElement) {
        try {
            val id = jsonElement.jsonObject["id"]?.jsonPrimitive?.intOrNull ?: return
            println("Pong received: $id")
        } catch (e: Exception) {
            emitError("Pong handler error: ${e.message}")
        }
    }
}