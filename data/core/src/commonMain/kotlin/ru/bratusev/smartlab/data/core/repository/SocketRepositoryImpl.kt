package ru.bratusev.smartlab.data.core.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import ru.bratusev.smartlab.data.core.HomeAssistantWebSocketClient
import ru.bratusev.smartlab.data.core.mapper.mapToDomain
import ru.bratusev.smartlab.data.core.model.SocketResponseModel
import ru.bratusev.smartlab.domain.core.model.socket.Area
import ru.bratusev.smartlab.domain.core.model.socket.ServiceEntity
import ru.bratusev.smartlab.domain.core.repository.SocketRepository

class
SocketRepositoryImpl(
    private val webSocketClient: HomeAssistantWebSocketClient
) : SocketRepository {
    override fun observeServiceEntities(): Flow<List<ServiceEntity>> =
        webSocketClient.socketResponseFlow.filter { it is SocketResponseModel.DeviceEntity }.map {
            (it as SocketResponseModel.DeviceEntity).services.map { it.mapToDomain() }
        }

    override fun observeAreaDevices(areaId: String): Flow<List<ServiceEntity>> {
        webSocketClient.sender.fetchAreaDevices(areaId)
        return webSocketClient.socketResponseFlow.filter { it is SocketResponseModel.AreaDeviceEntity }.map {
            (it as SocketResponseModel.AreaDeviceEntity).areaDevices.map { it.mapToDomain() }
        }
    }

    override fun observeAreas(): Flow<List<Area>> {
        webSocketClient.sender.fetchAreas()
        return webSocketClient.socketResponseFlow
            .filter { it is SocketResponseModel.AreasEntity }
            .map { (it as SocketResponseModel.AreasEntity).areas.map { it.mapToDomain() } }
    }
    override fun observeSocketErrors(): Flow<List<Error>> = webSocketClient.socketResponseFlow
        .filter { it is SocketResponseModel.ErrorMessage }
        .map { (it as SocketResponseModel.ErrorMessage).errors }

    override fun updateSensor(sensorId: String): Boolean {
        webSocketClient.serviceEntityCopy?.services?.find { it.id == sensorId }?.let {
            webSocketClient.sender.updateSensorState(it)
            return true
        }
        return false
    }
}