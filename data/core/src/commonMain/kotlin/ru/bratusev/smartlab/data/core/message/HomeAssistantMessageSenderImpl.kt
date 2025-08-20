package ru.bratusev.smartlab.data.core.message

import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.websocket.Frame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import ru.bratusev.smartlab.data.core.model.ServiceData
import ru.bratusev.smartlab.data.core.model.ServiceEntity
import ru.bratusev.smartlab.data.core.model.SocketMessage

class HomeAssistantMessageSenderImpl(
    private val json: Json,
    private val session: () -> DefaultClientWebSocketSession?,
    private val messageId: () -> Int,
    private val incrementMessageId: () -> Unit,
    private val errorFlow: MutableSharedFlow<List<Error>>
) : HomeAssistantMessageSender {

    override fun updateSensorState(sensor: ServiceEntity) {
        try {
            incrementMessageId()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val currentSession = session()
                    if (currentSession == null) {
                        errorFlow.tryEmit(listOf(Error("Cannot send sensor update: session is null")))
                        return@launch
                    }
                    sensor.toMsg()?.let {
                        currentSession.send(Frame.Text(json.encodeToString(it)))
                    }
                } catch (e: Exception) {
                    errorFlow.tryEmit(listOf(Error("Failed to send sensor update: ${e.message ?: e.toString()}")))
                }
            }
        } catch (e: Exception) {
            errorFlow.tryEmit(listOf(Error("Failed to build sensor update: ${e.message ?: e.toString()}")))
        }
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
}
