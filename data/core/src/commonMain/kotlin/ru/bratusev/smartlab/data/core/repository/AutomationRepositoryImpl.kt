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
import ru.bratusev.smartlab.data.core.YamlParser
import ru.bratusev.smartlab.data.core.mapper.mapToData
import ru.bratusev.smartlab.data.core.mapper.mapToDomain
import ru.bratusev.smartlab.data.core.remote_storage.Constants.BASE_URL
import ru.bratusev.smartlab.data.core.remote_storage.Constants.CONFIG_FILE_PATH
import ru.bratusev.smartlab.domain.core.model.automation.Automation
import ru.bratusev.smartlab.domain.core.repository.AutomationRepository
import ru.bratusev.smartlab.domain.core.repository.SocketRepository

class AutomationRepositoryImpl(
    val client: HttpClient,
    val socketRepository: SocketRepository
) : AutomationRepository {

    override suspend fun saveAutomation(automationData: List<Automation>): String {
        val automationString = YamlParser.parseAutomationsToYaml(automationData.map { it.mapToData() })
        val requestBody = Parameters.build {
            append("filename", CONFIG_FILE_PATH)
            append("text", automationString)
        }.formUrlEncode()

        // TODO Uncomment save request
        //val saveFileUrl = "$BASE_URL$shortFileUrl/api/save"
        //return client.post(saveFileUrl) {
        //    cookie("ingress_session", sessionId)
        //    contentType(ContentType.Application.FormUrlEncoded)
        //    setBody(requestBody)
        //}.bodyAsText()
        return "success"
    }

    override suspend fun fetchAutomaton(url: String): List<Automation> {
        shortFileUrl = url.replace("\"", "")
        val fileUrl = "$BASE_URL$shortFileUrl/api/file?filename=$CONFIG_FILE_PATH"
        socketRepository.fetchIngressSessionId().let { id ->
            sessionId = id.replace("\"", "")
            client.get(fileUrl) {
                contentType(ContentType.Application.Yaml)
                cookie("ingress_session", sessionId)
            }.bodyAsText().let { yaml ->
                return try {
                    YamlParser.parseAutomations(yaml).map { it.mapToDomain() }
                } catch (_ : Exception) {
                    emptyList()
                }
            }
        }
    }

    companion object {
        var sessionId: String = ""
        var shortFileUrl: String = ""
    }
}