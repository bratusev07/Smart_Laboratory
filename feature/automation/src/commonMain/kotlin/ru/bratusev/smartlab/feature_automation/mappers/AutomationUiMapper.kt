package ru.bratusev.smartlab.feature_automation.mappers

import ru.bratusev.smartlab.domain.core.model.socket.ServiceEntity
import ru.bratusev.smartlab.ui.core.models.SensorEntityUi

internal fun ServiceEntity.mapToUi() = SensorEntityUi(
    displayName = attributes?.friendlyName ?: id ?: "empty name",
    domain = domain.orEmpty(),
    entityId = id.orEmpty(),
)