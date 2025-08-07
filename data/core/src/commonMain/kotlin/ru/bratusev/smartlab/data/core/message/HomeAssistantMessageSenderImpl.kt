package ru.bratusev.smartlab.data.core.message

import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.websocket.Frame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import ru.bratusev.smartlab.data.core.model.ServiceData
import ru.bratusev.smartlab.data.core.model.SocketMessage

class HomeAssistantMessageSenderImpl(
    private val json: Json,
    private val session: () -> DefaultClientWebSocketSession?,
    private val messageId: () -> Int,
    private val incrementMessageId: () -> Unit,
    private val errorFlow: MutableSharedFlow<List<Error>>
) : HomeAssistantMessageSender {

    override fun updateSwitchState(switchId: String, switchState: String) {
        try {
            incrementMessageId()
            val message = SocketMessage.SwitchMsg(
                service = switchState,
                serviceData = ServiceData(switchId),
                id = messageId()
            )
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val currentSession = session()
                    if (currentSession == null) {
                        errorFlow.tryEmit(listOf(Error("Cannot send switch update: session is null")))
                        return@launch
                    }
                    currentSession.send(Frame.Text(json.encodeToString(message)))
                } catch (e: Exception) {
                    errorFlow.tryEmit(listOf(Error("Failed to send switch update: ${e.message ?: e.toString()}")))
                }
            }
        } catch (e: Exception) {
            errorFlow.tryEmit(listOf(Error("Failed to build switch update: ${e.message ?: e.toString()}")))
        }
    }
}
