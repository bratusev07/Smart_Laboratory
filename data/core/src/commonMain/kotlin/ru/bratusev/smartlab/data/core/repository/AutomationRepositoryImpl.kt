package ru.bratusev.smartlab.data.core.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.ktor.client.HttpClient
import io.ktor.client.request.cookie
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.formUrlEncode
import io.ktor.http.isSuccess
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
    private val client: HttpClient,
    private val socketRepository: SocketRepository,
    private val dataStore: DataStore<Preferences>,
    private val serverSelectionRepository: ServerSelectionRepository,
    private val authTokensStore: AuthTokensStore
) : AutomationRepository {

    private var sessionId: String = ""
    private var shortFileUrl: String = ""
    private var automationOriginString: String = ""

    override suspend fun fetchAutomaton(url: String): List<Automation> {
        return try {
            shortFileUrl = url.replace("\"", "")
            val rawSessionId = socketRepository.fetchIngressSessionId()
            sessionId = rawSessionId.replace("\"", "")

            val saveFileUrl = buildIngressUrl("/api/file", "?filename=$CONFIG_FILE_PATH")

            val response = client.get(saveFileUrl) {
                cookie("ingress_session", sessionId)
            }

            if (!response.status.isSuccess()) {
                println("ERROR (Fetch Automations): HTTP Status ${response.status}")
                return emptyList()
            }

            val yamlText = response.bodyAsText()
            automationOriginString = yamlText

            val fixedYaml = yamlText.replace("  choose:", "- choose:")
            YamlParser.parseAutomations(fixedYaml).map { it.mapToDomain() }

        } catch (e: Exception) {
            println("CRITICAL ERROR (Fetch Automations): ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun saveAutomation(automationData: List<Automation>): String {
        return try {
            val automationString = YamlParser.parseAutomationsToYaml(automationData.map { it.mapToData() })
            val newAutomationString = automationString.replace("- choose:", "  choose:").replace("  if:", "- if:")

            val requestBody = Parameters.build {
                append("filename", CONFIG_FILE_PATH)
                append("text", newAutomationString)
            }.formUrlEncode()

            val saveFileUrl = buildIngressUrl("/api/save")

            val response = client.post(saveFileUrl) {
                cookie("ingress_session", sessionId)
                contentType(ContentType.Application.FormUrlEncoded)
                setBody(requestBody)
            }

            val responseBody = response.bodyAsText()

            if (response.status.isSuccess() && responseBody.contains("success")) {
                reloadAutomationConfig()
                "success"
            } else {
                println("ERROR (Save Automation): HA rejected save. Body: $responseBody")
                rollbackAutomation()
                "error"
            }
        } catch (e: Exception) {
            println("CRITICAL ERROR (Save Automation): ${e.message}")
            rollbackAutomation()
            "error"
        }
    }

    private suspend fun reloadAutomationConfig() {
        try {
            val token = authTokensStore.loadTokens(dataStore)?.accessToken ?: return

            // ИСПРАВЛЕНИЕ ОШИБКИ КОМПИЛЯТОРА: безопасный вызов ?.
            val baseUrl = serverSelectionRepository.getCurrentBaseUrl()?.removeSuffix("/") ?: return

            val response = client.post("$baseUrl/api/services/automation/reload") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                    append(HttpHeaders.ContentType, "application/json")
                }
                setBody("{}")
            }

            if (!response.status.isSuccess()) {
                println("ERROR (Reload Automation): HTTP Status ${response.status}")
            }
        } catch (e: Exception) {
            println("ERROR (Reload Automation): ${e.message}")
        }
    }

    private suspend fun rollbackAutomation() {
        if (automationOriginString.isEmpty()) return

        try {
            val saveFileUrl = buildIngressUrl("/api/save")
            val requestBodyOrigin = Parameters.build {
                append("filename", CONFIG_FILE_PATH)
                append("text", automationOriginString)
            }.formUrlEncode()

            client.post(saveFileUrl) {
                cookie("ingress_session", sessionId)
                contentType(ContentType.Application.FormUrlEncoded)
                setBody(requestBodyOrigin)
            }
            println("Rollback to origin successful.")
        } catch (e: Exception) {
            println("CRITICAL ERROR (Rollback): Failed to restore origin. ${e.message}")
        }
    }

    private fun buildIngressUrl(endpoint: String, queryParams: String = ""): String {
        // ИСПРАВЛЕНИЕ ОШИБКИ КОМПИЛЯТОРА: безопасный вызов ?. и подстановка пустой строки, если null
        val baseUrl = serverSelectionRepository.getCurrentBaseUrl()?.removeSuffix("/") ?: ""
        val safeShortUrl = shortFileUrl.removePrefix("/")
        return "$baseUrl/$safeShortUrl$endpoint$queryParams"
    }
}