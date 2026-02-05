package ru.bratusev.smartlab.data.core.model

import kotlinx.serialization.Serializable
import ru.bratusev.smartlab.domain.core.model.ServerSelection

@Serializable
data class ServerSelectionEntity(
    val servers: Map<String, String>,
    val currentServerUrl: String?
) {
    fun toDomain(): ServerSelection = ServerSelection(servers, currentServerUrl)

    companion object {
        fun fromDomain(domainModel: ServerSelection): ServerSelectionEntity =
            ServerSelectionEntity(domainModel.servers, domainModel.currentServerUrl)
    }
}