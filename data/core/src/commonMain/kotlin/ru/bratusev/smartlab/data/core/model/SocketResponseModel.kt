package ru.bratusev.smartlab.data.core.model

sealed class SocketResponseModel {

    data class DeviceEntity(val services: List<ServiceEntity>): SocketResponseModel()

    data class AreaDeviceEntity(val areaDevices: List<ServiceEntity>): SocketResponseModel()

    data class ErrorMessage(val errors: List<Error>): SocketResponseModel()

    data class AreasEntity(val areas: List<AreaEntity>): SocketResponseModel()
}