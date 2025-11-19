package ru.bratusev.smartlab.domain.core.repository

import kotlinx.coroutines.flow.Flow
import ru.bratusev.smartlab.domain.core.model.socket.Area
import ru.bratusev.smartlab.domain.core.model.socket.ServiceEntity

interface SocketRepository {
    fun observeServiceEntities(): Flow<List<ServiceEntity>>

    fun observeAreaDevices(areaId: String): Flow<List<ServiceEntity>>

    fun observeAreas(): Flow<List<Area>>

    fun observeSocketErrors(): Flow<List<Error>>

    fun updateSensor(sensorId: String): Boolean

    suspend fun fetchAutomation(): String
}
