package ru.bratusev.smartlab.data.core.model

import kotlinx.serialization.Serializable
import ru.bratusev.smartlab.domain.core.model.ServerSelection

@Serializable
data class ServerSelectionEntity(
    val servers: List<Server>, val currentServerUrl: String?, val currentServerName: String?
) {
    @Serializable
    data class Server(
        val url: String, val name: String, val login: String, val password: String
    )

    fun toDomain(): ServerSelection = ServerSelection(buildList {
        servers.forEach { server ->
            add(listOf(server.url, server.name, server.login, server.password))
        }
    }, currentServerUrl, currentServerName)

    companion object {
        fun fromDomain(domainModel: ServerSelection): ServerSelectionEntity =
            ServerSelectionEntity(domainModel.servers.map { serverArr ->
                Server(
                    url = serverArr[0],
                    name = serverArr[1],
                    login = serverArr[2],
                    password = serverArr[3]
                )
            }, domainModel.currentServerUrl, domainModel.currentServerName)
    }
}