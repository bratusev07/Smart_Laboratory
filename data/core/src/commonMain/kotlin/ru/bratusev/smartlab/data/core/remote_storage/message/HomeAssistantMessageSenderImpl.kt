package ru.bratusev.smartlab.data.core.remote_storage.message

import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.websocket.Frame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import ru.bratusev.smartlab.data.core.model.ServiceData
import ru.bratusev.smartlab.data.core.model.ServiceEntity
import ru.bratusev.smartlab.data.core.model.SocketMessage
import ru.bratusev.smartlab.data.core.model.SocketResponseModel

class HomeAssistantMessageSenderImpl(
    private val json: Json,
    private val session: () -> DefaultClientWebSocketSession?,
    private val messageId: () -> Int,
    private val incrementMessageId: () -> Unit,
    private val errorFlow: MutableSharedFlow<SocketResponseModel>
) : HomeAssistantMessageSender {

    override fun updateSensorState(sensor: ServiceEntity) {
        sendMessage(
            buildMessage = { sensor.toMsg() },
            actionName = "send sensor update",
            onSessionNull = { "Cannot send sensor update: session is null" },
            onBuildFailure = { "Failed to build sensor update: ${it.message ?: it.toString()}" },
            onSendFailure = { "Failed to send sensor update: ${it.message ?: it.toString()}" }
        )
    }

    override fun fetchAreas() {
        sendMessage<SocketMessage.FetchAreasMsg>(
            buildMessage = { SocketMessage.FetchAreasMsg(id = messageId()) },
            actionName = "fetch areas"
        )
    }

    override fun fetchAreaDevices(areaId: String) {
        sendMessage(
            buildMessage = { SocketMessage.FetchAreaDevicesMsg(id = messageId(), itemId = areaId) },
            actionName = "fetch area devices"
        )
    }

    override fun fetchAutomations() {
        sendMessage(
            buildMessage = { SocketMessage.ConfigurationInfo(id = messageId()) },
            actionName = "fetch automations"
        )
    }

    override fun fetchIngressSessionId() {
        sendMessage(
            buildMessage = { SocketMessage.IngressSession(id = messageId()) },
            actionName = "fetch ingress session id"
        )
    }

    private fun ServiceEntity.toMsg(): SocketMessage.SensorMsg? {
        return SocketMessage.SensorMsg(
            domain = domain.orEmpty(),
            service = state?.updateState().orEmpty(),
            serviceData = ServiceData(id.orEmpty()),
            id = messageId()
        )
    }

    private fun JsonElement?.updateState() =
        if (this.toString().contains("off")) "turn_on"
        else if (this.toString().contains("on")) "turn_off"
        else "press"

    private inline fun <reified T : Any> sendMessage(
        noinline buildMessage: () -> T?,
        actionName: String,
        crossinline onSessionNull: () -> String = { "Cannot $actionName: session is null" },
        onBuildFailure: (Exception) -> String = { "Failed to $actionName: ${it.message ?: it.toString()}" },
        crossinline onSendFailure: (Exception) -> String = { "Failed to $actionName: ${it.message ?: it.toString()}" }
    ) {
        try {
            incrementMessageId()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val currentSession = session()
                    if (currentSession == null) {
                        errorFlow.tryEmit(SocketResponseModel.ErrorMessage(listOf(Error(onSessionNull()))))
                        return@launch
                    }

                    val message = buildMessage()
                    if (message != null) {
                        currentSession.send(Frame.Text(json.encodeToString(message)))
                    }
                } catch (e: Exception) {
                    errorFlow.tryEmit(SocketResponseModel.ErrorMessage(listOf(Error(onSendFailure(e)))))
                }
            }
        } catch (e: Exception) {
            errorFlow.tryEmit(SocketResponseModel.ErrorMessage(listOf(Error(onBuildFailure(e)))))
        }
    }
}
