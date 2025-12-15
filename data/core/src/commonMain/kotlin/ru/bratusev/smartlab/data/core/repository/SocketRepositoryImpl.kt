package ru.bratusev.smartlab.data.core.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import ru.bratusev.smartlab.data.core.mapper.mapToDomain
import ru.bratusev.smartlab.data.core.model.SocketResponseModel
import ru.bratusev.smartlab.data.core.remote_storage.HomeAssistantWebSocketClient
import ru.bratusev.smartlab.domain.core.model.socket.Area
import ru.bratusev.smartlab.domain.core.model.socket.ServiceEntity
import ru.bratusev.smartlab.domain.core.repository.SocketRepository

class SocketRepositoryImpl(
    private val webSocketClient: HomeAssistantWebSocketClient,
    private val scope: CoroutineScope
) : SocketRepository {

    private val servicesFlow by lazy {
        webSocketClient.socketResponseFlow
            .filterIsInstance<SocketResponseModel.DeviceEntity>()
            .map { it.services.map { s -> s.mapToDomain() } }
            .shareIn(scope, SharingStarted.WhileSubscribed(5000), replay = 1)
    }

    override fun observeServiceEntities(): Flow<List<ServiceEntity>> = servicesFlow

    private val areaDevicesFlows = mutableMapOf<String, SharedFlow<List<ServiceEntity>>>()

    override fun observeAreaDevices(areaId: String): Flow<List<ServiceEntity>> =
        areaDevicesFlows.getOrPut(areaId) {
            webSocketClient.socketResponseFlow
                .filterIsInstance<SocketResponseModel.AreaDeviceEntity>()
                .map { it.areaDevices.map { d -> d.mapToDomain() } }
                .shareIn(scope, SharingStarted.WhileSubscribed(5000), replay = 1)
                .also {
                    scope.launch {
                        webSocketClient.sender.fetchAreaDevices(areaId)
                    }
                }
        }

    private val areasFlow by lazy {
        webSocketClient.socketResponseFlow
            .filterIsInstance<SocketResponseModel.AreasEntity>()
            .map { it.areas.map { a -> a.mapToDomain() } }
            .shareIn(scope, SharingStarted.WhileSubscribed(5000), replay = 1)
            .also {
                scope.launch {
                    webSocketClient.sender.fetchAreas()
                }
            }
    }

    override fun observeAreas(): Flow<List<Area>> = areasFlow

    override fun observeSocketErrors(): Flow<List<Error>> =
        webSocketClient.socketResponseFlow
            .filterIsInstance<SocketResponseModel.ErrorMessage>()
            .map { it.errors }

    override fun updateSensor(sensorId: String): Boolean {
        val sensor = webSocketClient.serviceEntityCopy
            ?.services
            ?.find { it.id == sensorId }

        return if (sensor != null) {
            webSocketClient.sender.updateSensorState(sensor)
            true
        } else false
    }

    override suspend fun fetchAutomation(): String {
        webSocketClient.sender.fetchAutomations()

        return webSocketClient.socketResponseFlow
            .filterIsInstance<SocketResponseModel.AutomationUrl>()
            .map { it.url }
            .first()
    }

    override suspend fun fetchIngressSessionId(): String {
        webSocketClient.sender.fetchIngressSessionId()

        return webSocketClient.socketResponseFlow
            .filterIsInstance<SocketResponseModel.IngressSessionId>()
            .map { it.id }
            .first()
    }
}