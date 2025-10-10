package ru.bratusev.smartlab.data.core.repository.preview

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.bratusev.smartlab.domain.core.model.socket.Area
import ru.bratusev.smartlab.domain.core.repository.SocketRepository
import ru.bratusev.smartlab.domain.core.model.socket.ServiceEntity as DomainServiceEntity

class SocketRepositoryPreview(
) : SocketRepository {
    private val devices = MutableStateFlow(emptyList<DomainServiceEntity>())

    init {
        val data = buildList {
            for (i in 1..30) {
                add(
                    DomainServiceEntity(
                        state = listOf("on", "off", null).random(),
                        attributes = null,
                        c = null,
                        id = "Id$i",
                        domain = "switch",
                        lastUpdate = null,
                        lastChange = null
                    )
                )
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            devices.emit(data)
        }
    }

    override fun observeServiceEntities(): Flow<List<DomainServiceEntity>> =
        devices.asSharedFlow().map { list -> list.map { it } }

    override fun observeAreaDevices(areaId: String): Flow<List<DomainServiceEntity>> {
        TODO("Not yet implemented")
    }

    override fun observeAreas(): Flow<List<Area>> {
        TODO("Not yet implemented")
    }

    override fun observeSocketErrors(): Flow<List<Error>> = emptyFlow()

    override fun updateSensor(sensorId: String): Boolean {
        println("✅ Preview: Changing sensorState $sensorId")
        val updatedDevices = devices.value.map {
            if (it.id == sensorId) {
                it.copy(state = if (it.state == "off") "on" else "off")
            } else {
                it
            }
        }
        devices.value.find { it.id == sensorId }?.let {
            CoroutineScope(Dispatchers.IO).launch {
                devices.emit(updatedDevices)
            }
            return true
        }
        return false
    }

}