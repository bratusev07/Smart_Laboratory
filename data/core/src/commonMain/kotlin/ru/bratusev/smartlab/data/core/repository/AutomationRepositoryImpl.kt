package ru.bratusev.smartlab.data.core.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.cookie
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.formUrlEncode
import ru.bratusev.smartlab.data.core.remote_storage.Constants.BASE_URL
import ru.bratusev.smartlab.data.core.remote_storage.Constants.CONFIG_FILE_PATH
import ru.bratusev.smartlab.domain.core.repository.AutomationRepository
import ru.bratusev.smartlab.domain.core.repository.SocketRepository

class AutomationRepositoryImpl(
    val client: HttpClient,
    val socketRepository: SocketRepository
) : AutomationRepository {

    override suspend fun saveAutomation(automationData: String) {
        val requestBody = Parameters.build {
            append("filename", CONFIG_FILE_PATH)
            append("text", automationData)
        }.formUrlEncode()

        val saveFileUrl = "$BASE_URL$shortFileUrl/api/save"
        client.post(saveFileUrl) {
            cookie("ingress_session", sessionId)
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(requestBody)
        }.bodyAsText()
    }

    override suspend fun fetchAutomaton(url: String) {
        shortFileUrl = url.replace("\"", "")
        val fileUrl = "$BASE_URL$shortFileUrl/api/file?filename=$CONFIG_FILE_PATH"
        socketRepository.fetchIngressSessionId().let { id ->
            sessionId = id.replace("\"", "")
            client.get(fileUrl) {
                contentType(ContentType.Application.Yaml)
                cookie("ingress_session", sessionId)
            }.bodyAsText().let {}
        }
    }

    companion object {
        var sessionId: String = ""
        var shortFileUrl: String = ""
    }
}