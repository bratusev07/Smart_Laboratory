package ru.bratusev.smartlab.data.core.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.cookie
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.bratusev.smartlab.data.core.remote_storage.Constants.BASE_URL
import ru.bratusev.smartlab.data.core.remote_storage.Constants.CONFIG_FILE_PATH
import ru.bratusev.smartlab.domain.core.repository.AutomationRepository
import ru.bratusev.smartlab.domain.core.repository.SocketRepository

class AutomationRepositoryImpl(val client: HttpClient, val socketRepository: SocketRepository): AutomationRepository {

    override suspend fun saveAutomation() {

    }

    override suspend fun fetchAutomaton(url: String) {
        val fileUrl = "$BASE_URL${url.replace("\"", "")}/api/file?filename=$CONFIG_FILE_PATH"
        socketRepository.fetchIngressSessionId().let { sessionId ->
            val response = client.get(fileUrl) {
                contentType(ContentType.Application.Yaml)
                cookie("ingress_session", sessionId.replace("\"", ""))
            }.bodyAsText()
        }
    }
}