package ru.bratusev.smartlab.data.core.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import ru.bratusev.smartlab.data.core.mapper.AreaEntityMapper
import ru.bratusev.smartlab.data.core.mapper.mapToDomain
import ru.bratusev.smartlab.data.core.model.SocketResponseModel
import ru.bratusev.smartlab.data.core.remote_storage.HomeAssistantWebSocketClient
import ru.bratusev.smartlab.domain.core.model.socket.Area
import ru.bratusev.smartlab.domain.core.model.socket.ServiceEntity
import ru.bratusev.smartlab.domain.core.repository.SocketRepository

class SocketRepositoryImpl(
    private val webSocketClient: HomeAssistantWebSocketClient,
    private val scope: CoroutineScope,
    private val areaEntityMapper: AreaEntityMapper
) : SocketRepository {

    // Явное указание типа SharedFlow помогает компилятору
    private val servicesFlow: SharedFlow<List<ServiceEntity>> by lazy {
        webSocketClient.socketResponseFlow
            .filter { it is SocketResponseModel.DeviceEntity }
            .map { (it as SocketResponseModel.DeviceEntity).services.map { s -> s.mapToDomain() } }
            .shareIn(scope, SharingStarted.WhileSubscribed(5000), replay = 1)
    }

    override fun observeServiceEntities(): Flow<List<ServiceEntity>> = servicesFlow

    private val areaDevicesFlows = mutableMapOf<String, SharedFlow<List<ServiceEntity>>>()

    override fun observeAreaDevices(areaId: String): Flow<List<ServiceEntity>> =
        areaDevicesFlows.getOrPut(areaId) {
            webSocketClient.socketResponseFlow
                .filter { it is SocketResponseModel.AreaDeviceEntity }
                .map { (it as SocketResponseModel.AreaDeviceEntity).areaDevices.map { d -> d.mapToDomain() } }
                .shareIn(scope, SharingStarted.WhileSubscribed(5000), replay = 1)
                .also {
                    scope.launch {
                        webSocketClient.sender.fetchAreaDevices(areaId)
                    }
                }
        }

    // Явное указание типа SharedFlow
    private val areasFlow: SharedFlow<List<Area>> by lazy {
        webSocketClient.socketResponseFlow
            .filter { it is SocketResponseModel.AreasEntity }
            .map { (it as SocketResponseModel.AreasEntity).areas.map { a -> areaEntityMapper.mapToDomain(a) } }
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
            .filter { it is SocketResponseModel.ErrorMessage }
            .map { (it as SocketResponseModel.ErrorMessage).errors }

    override fun updateSensor(sensorId: String): Boolean {
        val sensor = webSocketClient.serviceEntityCopy
            ?.services
            ?.find { it.id == sensorId }

        return if (sensor != null) {
            webSocketClient.sender.updateSensorState(sensor)
            true
        } else false
    }

    // Явное указание async<String> для компилятора
    override suspend fun fetchAutomation(): String = coroutineScope {
        val deferredResponse = async<String> {
            webSocketClient.socketResponseFlow
                .filter { it is SocketResponseModel.AutomationUrl }
                .map { (it as SocketResponseModel.AutomationUrl).url }
                .first()
        }

        webSocketClient.sender.fetchAutomations()

        deferredResponse.await()
    }

    // Явное указание async<String> для компилятора
    override suspend fun fetchIngressSessionId(): String = coroutineScope {
        val deferredResponse = async<String> {
            webSocketClient.socketResponseFlow
                .filter { it is SocketResponseModel.IngressSessionId }
                .map { (it as SocketResponseModel.IngressSessionId).id }
                .first()
        }

        webSocketClient.sender.fetchIngressSessionId()

        deferredResponse.await()
    }
}