package ru.bratusev.smartlab.domain.core.repository

import kotlinx.coroutines.flow.Flow
import ru.bratusev.smartlab.domain.core.model.socket.ServiceEntity

interface SocketRepository {
    fun observeServiceEntities(): Flow<List<ServiceEntity>>

    fun observeSocketErrors(): Flow<List<Error>>

    fun updateSensor(sensorId: String): Boolean
}
