package ru.bratusev.smartlab.data.core.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.formUrlEncode
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import ru.bratusev.smartlab.data.core.Constants.BASE_URL
import ru.bratusev.smartlab.data.core.HomeAssistantWebSocketClient
import ru.bratusev.smartlab.domain.core.model.Device
import ru.bratusev.smartlab.domain.core.repository.AuthRepository

class AuthRepositoryImpl(
    private val client: HttpClient,
    private val socketClient: HomeAssistantWebSocketClient,
    private val dataStore: DataStore<Preferences>
) : AuthRepository {

    override suspend fun login(login: String, password: String): String {
        return auth(login, password)
    }

    private suspend fun providers() {
        val response = client.get("$BASE_URL/auth/providers")
        println(response.bodyAsText())
    }

    private suspend fun auth(login: String, password: String): String {
        val requestBody = LoginFlowRequestBody(
            client_id = "https://home-assistant.io/android",
            handler = listOf("homeassistant", null),
            redirect_uri = "homeassistant://auth-callback"
        )

        val response = client.post("$BASE_URL/auth/login_flow") {
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }

        return authFlow(
            flowId = Json.parseToJsonElement(response.bodyAsText())
                .jsonObject["flow_id"]
                .toString()
                .replace("\"", ""),
            login = login,
            password = password,
        )
    }

    private suspend fun authFlow(flowId: String, login: String, password: String): String {
        val requestBody = LoginFlowRequestBodyWithId(
            client_id = "https://home-assistant.io/android",
            username = login,
            password = password
        )

        val response = client.post("$BASE_URL/auth/login_flow/$flowId") {
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }

        return token(
            code = Json.parseToJsonElement(response.bodyAsText())
                .jsonObject["result"]
                .toString()
                .replace("\"", ""),
        )
    }

    private suspend fun token(code: String): String {
        val requestBody = Parameters.build {
            append("grant_type", "authorization_code")
            append("code", code)
            append("client_id", "https://home-assistant.io/android")
        }.formUrlEncode()

        val response = client.post("$BASE_URL/auth/token") {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(requestBody)
        }

        val token = Json.parseToJsonElement(response.bodyAsText())
            .jsonObject["access_token"]
            .toString()
            .replace("\"", "")

        socketClient.setToken(token).connect()
        return token
    }

    // To Auth interface
    @Deprecated("Не работает GET запрос с Body")
    override suspend fun config(token: String) {
        val requestBody = ConfigRequestBody(
            client_id = "https://hass.io",
            handler = listOf("homeassistant", null),
            redirect_uri = "https://hass.io",
            type = "authorize"
        )

        try {
            val response = client.request("$BASE_URL/api/config") {
                method = HttpMethod("GET")
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }
            println(response.bodyAsText())
        } catch (e: Exception) {
            e
        }
    }

    // To Auth interface
    override suspend fun registrations(token: String, device: Device) {
        val requestBody = RegistrationRequest(
            app_id = device.appId,
            app_name = device.appName,
            app_version = device.appVersion,
            device_name = device.deviceName,
            manufacturer = device.manufacturer,
            model = device.model,
            os_name = device.osName,
            os_version = device.osVersion,
            supports_encryption = false,
            app_data = AppData(
                push_websocket_channel = true,
                push_url = "https://mobile-apps.home-assistant.io/api/sendPush/android/v1",
                push_token = "fmOUYFDQRB6TcOZ7WxjdQS:APA91bFS7_AKgoOYdVSZYBKbupVre06QGW5gGXguG8hSrWv9V4VjKS_3R1e1n8SKGaobeRloXSWfkFmgh4Og28-W_RLwzBlg2OL8zHn2kRn4VOUnlid1nQg"
            ),
            device_id = device.deviceId
        )

        val response = client.post("$BASE_URL/api/mobile_app/registrations") {
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }

        val webhookId = Json.parseToJsonElement(response.bodyAsText())
            .jsonObject["webhook_id"]
            .toString()
            .replace("\"", "")
    }

    override suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    override suspend fun getToken(): String? {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }.first()
    }

    private companion object {
        val TOKEN_KEY = stringPreferencesKey("auth_token")
    }
}

@Serializable
data class LoginFlowRequestBody(
    val client_id: String,
    val handler: List<String?>,
    val redirect_uri: String
)

@Serializable
data class LoginFlowRequestBodyWithId(
    val username: String,
    val password: String,
    val client_id: String
)

@Serializable
data class ConfigRequestBody(
    val client_id: String,
    val handler: List<String?>,
    val redirect_uri: String,
    val type: String
)


@Serializable
data class RegistrationRequest(
    val app_id: String,
    val app_name: String,
    val app_version: String,
    val device_name: String,
    val manufacturer: String,
    val model: String,
    val os_name: String,
    val os_version: String,
    val supports_encryption: Boolean,
    val app_data: AppData,
    val device_id: String
)

@Serializable
data class AppData(
    val push_websocket_channel: Boolean,
    val push_url: String,
    val push_token: String
)