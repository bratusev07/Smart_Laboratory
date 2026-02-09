package ru.bratusev.smartlab.data.core.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.ktor.client.HttpClient
import io.ktor.client.request.cookie
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.formUrlEncode
import ru.bratusev.smartlab.data.core.YamlParser
import ru.bratusev.smartlab.data.core.local_storage.dataStore.AuthTokensStore
import ru.bratusev.smartlab.data.core.mapper.mapToData
import ru.bratusev.smartlab.data.core.mapper.mapToDomain
import ru.bratusev.smartlab.data.core.remote_storage.Constants.CONFIG_FILE_PATH
import ru.bratusev.smartlab.domain.core.model.automation.Automation
import ru.bratusev.smartlab.domain.core.repository.AutomationRepository
import ru.bratusev.smartlab.domain.core.repository.ServerSelectionRepository
import ru.bratusev.smartlab.domain.core.repository.SocketRepository

class AutomationRepositoryImpl(
    val client: HttpClient,
    val socketRepository: SocketRepository,
    private val dataStore: DataStore<Preferences>,
    private val serverSelectionRepository: ServerSelectionRepository,
    private val authTokensStore: AuthTokensStore
) : AutomationRepository {

    override suspend fun saveAutomation(automationData: List<Automation>): String {
        val automationString =
            YamlParser.parseAutomationsToYaml(automationData.map { it.mapToData() })
        val newAutomationString =
            automationString.replace("- choose:", "  choose:").replace("  if:", "- if:")
        val requestBody = Parameters.build {
            append("filename", CONFIG_FILE_PATH)
            append("text", newAutomationString)
        }.formUrlEncode()

        val saveFileUrl = "${serverSelectionRepository.getCurrentBaseUrl()}$shortFileUrl/api/save"
        val result = client.post(saveFileUrl) {
            cookie("ingress_session", sessionId)
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(requestBody)
        }.bodyAsText()

        if (!result.contains("success")) {
            if (result.contains("4")) return "error"
            val requestBodyOrigin = Parameters.build {
                append("filename", CONFIG_FILE_PATH)
                append("text", automationOriginString)
            }.formUrlEncode()

            client.post(saveFileUrl) {
                cookie("ingress_session", sessionId)
                contentType(ContentType.Application.FormUrlEncoded)
                setBody(requestBodyOrigin)
            }.bodyAsText()
            return "error"
        } else {
            reloadAutomationConfig()
            return "success"
        }
    }

    suspend fun reloadAutomationConfig() {
        try {
            val token = authTokensStore.loadTokens(dataStore)?.accessToken
            val response: HttpResponse =
                client.post("${serverSelectionRepository.getCurrentBaseUrl()}/api/services/automation/reload") {
                    headers {
                        append(HttpHeaders.Authorization, "Bearer $token")
                        append(HttpHeaders.ContentType, "application/json")
                    }
                    setBody("{}")
                }
            response.bodyAsText()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun fetchAutomaton(url: String): List<Automation> {
        shortFileUrl = url.replace("\"", "")
        val fileUrl =
            "${serverSelectionRepository.getCurrentBaseUrl()}$shortFileUrl/api/file?filename=$CONFIG_FILE_PATH"
        socketRepository.fetchIngressSessionId().let { id ->
            sessionId = id.replace("\"", "")
            client.get(fileUrl) {
                contentType(ContentType.Application.Yaml)
                cookie("ingress_session", sessionId)
            }.bodyAsText().let { yaml ->
                return try {
                    YamlParser.parseAutomations(yaml.replace("  choose:", "- choose:"))
                        .map { it.mapToDomain() }
                } catch (_: Exception) {
                    emptyList()
                }
            }
        }
    }

    companion object {
        var sessionId: String = ""
        var shortFileUrl: String = ""
        var automationOriginString: String = ""
    }
}