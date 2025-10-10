package ru.bratusev.smartlab.data.core.message

import ru.bratusev.smartlab.data.core.model.ServiceEntity

interface HomeAssistantMessageSender {
    fun updateSensorState(sensor: ServiceEntity)

    fun fetchAreas()

    fun fetchAreaDevices(areaId: String)
}