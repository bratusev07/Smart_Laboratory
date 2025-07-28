package ru.bratusev.smartlab.feature_login.mappers

import ru.bratusev.smartlab.feature_login.models.Device
import ru.bratusev.smartlab.domain.core.model.Device as DomainDevice

internal fun Device.toDomain() = DomainDevice(
    appId = appId,
    appName = appName,
    appVersion = appVersion,
    deviceName = deviceName,
    manufacturer = manufacturer,
    model = model,
    osName = osName,
    osVersion = osVersion,
    deviceId = deviceId
)