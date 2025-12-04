package ru.bratusev.smartlab.feature_login.mappers

import ru.bratusev.smartlab.domain.core.model.NetworkStatus
import ru.bratusev.smartlab.feature_login.models.Device
import ru.bratusev.smartlab.feature_login.models.NetworkStatusUi
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

internal fun NetworkStatus.toUi() = NetworkStatusUi(
    ip = this.ip,
    baseUrl = this.baseUrl,
    isVpnActive = this.isVpnActive
)