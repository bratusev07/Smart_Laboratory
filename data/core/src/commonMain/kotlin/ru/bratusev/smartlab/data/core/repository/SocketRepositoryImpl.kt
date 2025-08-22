package ru.bratusev.smartlab.data.core.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.bratusev.smartlab.data.core.HomeAssistantWebSocketClient
import ru.bratusev.smartlab.data.core.mapper.mapToDomain
import ru.bratusev.smartlab.domain.core.model.socket.ServiceEntity
import ru.bratusev.smartlab.domain.core.repository.SocketRepository

class
SocketRepositoryImpl(
    private val webSocketClient: HomeAssistantWebSocketClient
) : SocketRepository {
    override fun observeServiceEntities(): Flow<List<ServiceEntity>> =
        webSocketClient.serviceEntitiesFlow.map { list -> list.map { it.mapToDomain() } }

    override fun observeSocketErrors(): Flow<List<Error>> = webSocketClient.socketErrorsFlow

    override fun updateSensor(sensorId: String): Boolean {
        webSocketClient._serviceEntityCopy.find { it.id == sensorId }?.let {
            webSocketClient.sender.updateSensorState(it)
            return true
        }
        return false
    }

}