package ru.bratusev.smartlab.data.core.message

import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.websocket.Frame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
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
import ru.bratusev.smartlab.data.core.model.ResultResponseMsg
import ru.bratusev.smartlab.data.core.model.ServiceEntity
import ru.bratusev.smartlab.data.core.model.SocketMessage
import ru.bratusev.smartlab.data.core.model.SocketResponseModel
import ru.bratusev.smartlab.data.core.model.TriggerResponseMsg

class HomeAssistantMessageHandlersImpl(
    val json: Json,
    val errorFlow: MutableSharedFlow<SocketResponseModel>,
    val session: DefaultClientWebSocketSession?
) : HomeAssistantMessageHandlers {

    override fun handleAuthRequired(jsonElement: JsonElement, accessToken: String?) {
        try {
            val msg = json.decodeFromJsonElement<AuthRequiredResponseMsg>(jsonElement)
            println("Auth required. HA version: ${msg.haVersion}")
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    accessToken?.let {
                        session?.send(
                            Frame.Text(
                                SocketMessage.AuthMsg(type = "auth", accessToken = it).toString()
                            )
                        )
                    } ?: errorFlow.tryEmit(SocketResponseModel.ErrorMessage(listOf(Error("Access token is null during auth_required"))))
                } catch (e: Exception) {
                    errorFlow.tryEmit(SocketResponseModel.ErrorMessage(listOf(Error("Failed to send auth message: ${e.message ?: e.toString()}"))))
                }
            }
        } catch (e: Exception) {
            errorFlow.tryEmit(SocketResponseModel.ErrorMessage(listOf(Error("AuthRequired handler error: ${e.message ?: e.toString()}"))))
        }
    }

    override fun handleAuthOk(jsonElement: JsonElement, getMessageId: () -> Int, setMessageId: (Int) -> Unit) {
        try {
            val msg = json.decodeFromJsonElement<AuthOkResponseMsg>(jsonElement)
            println("Authenticated successfully. HA version: ${msg.haVersion}")
            val newId = getMessageId().plus(1)
            setMessageId(newId)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    session?.send(
                        Frame.Text(
                            SocketMessage.SubEntitiesMsg(
                                type = "subscribe_entities",
                                id = newId
                            ).toString()
                        )
                    )
                } catch (e: Exception) {
                    errorFlow.tryEmit(SocketResponseModel.ErrorMessage(listOf(Error("Failed to send subscribe_entities: ${e.message ?: e.toString()}"))))
                }
            }
        } catch (e: Exception) {
            errorFlow.tryEmit(SocketResponseModel.ErrorMessage(listOf(Error("AuthOk handler error: ${e.message ?: e.toString()}"))))
        }
    }

    override fun handleAuthInvalid(jsonElement: JsonElement) {
        try {
            val msg = json.decodeFromJsonElement<AuthInvalidResponseMsg>(jsonElement)
            println("Auth failed: ${msg.message}")
            errorFlow.tryEmit(SocketResponseModel.ErrorMessage(listOf(Error("Auth failed: ${msg.message}"))))
        } catch (e: Exception) {
            errorFlow.tryEmit(SocketResponseModel.ErrorMessage(listOf(Error("AuthInvalid handler error: ${e.message ?: e.toString()}"))))
        }
    }

    override fun handleResult(jsonElement: JsonElement, emitAreaEntity: (List<AreaEntity>) -> Boolean) {
        try {
            val id = jsonElement.jsonObject["id"]?.jsonPrimitive?.intOrNull ?: return
            val success = jsonElement.jsonObject["success"]?.jsonPrimitive?.booleanOrNull == true
            val error = jsonElement.jsonObject["error"]?.let {
                json.decodeFromJsonElement<ApiError>(it)
            }
            val result = jsonElement.jsonObject["result"]
            val resultResponseMsg = ResultResponseMsg(id, success, result, error)
            if (!success) {
                val message = "Command failed [${resultResponseMsg.id}]: ${resultResponseMsg.error?.message}"
                println(message)
                errorFlow.tryEmit(SocketResponseModel.ErrorMessage(listOf(Error(message))))
            }else {
                if (result != null && result is JsonArray && result.toString().contains("area_id")) {
                    try {
                        val areaEntities = result.map { jsonElement ->
                            json.decodeFromJsonElement<AreaEntity>(jsonElement)
                        }
                        emitAreaEntity(areaEntities)
                    } catch (e: Exception) {
                        errorFlow.tryEmit(SocketResponseModel.ErrorMessage(listOf(Error("Failed to decode AreaEntity list: ${e.message ?: e.toString()}"))))
                    }
                } else {
                    errorFlow.tryEmit(SocketResponseModel.ErrorMessage(listOf(Error("Result is not a valid JSON array"))))
                }
            }
        } catch (e: Exception) {
            errorFlow.tryEmit(SocketResponseModel.ErrorMessage(listOf(Error("Result handler error: ${e.message ?: e.toString()}"))))
        }
    }

    override fun handleEvent(
        jsonElement: JsonElement,
        getServiceEntitiesCopy: () -> List<ServiceEntity>,
        setServiceEntitiesCopy: (List<ServiceEntity>) -> Unit,
        emitServiceEntities: (List<ServiceEntity>) -> Boolean
    ) {
        try {
            val eventObj = jsonElement.jsonObject
            if (eventObj["event"]?.jsonObject?.get("event_type") != null) {
                val eventMsg = json.decodeFromJsonElement<EventResponseMsg>(jsonElement)
                println(eventMsg)
            } else if (eventObj["event"]?.jsonObject?.get("variables") != null) {
                val triggerMsg = json.decodeFromJsonElement<TriggerResponseMsg>(jsonElement)
                println(triggerMsg)
            } else if (eventObj["event"]?.jsonObject?.get("a") != null) {
                mapJsonToServiceEntityList(jsonElement.toString()).let { list ->
                    if (emitServiceEntities(list)) {
                        setServiceEntitiesCopy(list)
                    }
                }
            } else if (eventObj["event"]?.jsonObject?.get("c") != null) {
                mapJsonToEventPair(jsonElement.toString()).let { pair ->
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
            errorFlow.tryEmit(SocketResponseModel.ErrorMessage(listOf(Error("Event handler error: ${e.message ?: e.toString()}"))))
        }
    }

    override fun handlePong(jsonElement: JsonElement) {
        try {
            val id = jsonElement.jsonObject["id"]?.jsonPrimitive?.intOrNull ?: return
            println("Pong received: $id")
        } catch (e: Exception) {
            errorFlow.tryEmit(SocketResponseModel.ErrorMessage(listOf(Error("Pong handler error: ${e.message ?: e.toString()}"))))
        }
    }
}
