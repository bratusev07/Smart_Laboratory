package ru.bratusev.smartlab.data.core.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.ktor.client.HttpClient
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ru.bratusev.smartlab.data.core.local_storage.dataStore.AuthTokensStore
import ru.bratusev.smartlab.data.core.model.AppData
import ru.bratusev.smartlab.data.core.model.ConfigRequestBody
import ru.bratusev.smartlab.data.core.model.LoginFlowRequestBody
import ru.bratusev.smartlab.data.core.model.LoginFlowRequestBodyWithId
import ru.bratusev.smartlab.data.core.model.RegistrationRequest
import ru.bratusev.smartlab.data.core.remote_storage.HomeAssistantWebSocketClient
import ru.bratusev.smartlab.domain.core.model.Device
import ru.bratusev.smartlab.domain.core.repository.AuthRepository
import ru.bratusev.smartlab.domain.core.repository.ServerSelectionRepository

class AuthRepositoryImpl(
    private val client: HttpClient,
    private val socketClient: HomeAssistantWebSocketClient,
    private val dataStore: DataStore<Preferences>,
    private val serverSelectionRepository: ServerSelectionRepository,
    private val authTokensStore: AuthTokensStore
) : AuthRepository {

    private val loginFlowBody = LoginFlowRequestBody(
        client_id = "https://home-assistant.io/android",
        handler = listOf("homeassistant", null),
        redirect_uri = "homeassistant://auth-callback"
    )

    private fun loginFlowRequestBodyWithId(
        login: String,
        password: String
    ): LoginFlowRequestBodyWithId {
        return LoginFlowRequestBodyWithId(
            client_id = "https://home-assistant.io/android",
            username = login,
            password = password
        )
    }

    override suspend fun login(login: String, password: String) {
        auth(login, password)
    }

    private suspend fun auth(login: String, password: String) {
        client.post("${serverSelectionRepository.getCurrentBaseUrl()}/auth/login_flow") {
            contentType(ContentType.Application.Json)
            setBody(loginFlowBody)
        }.bodyAsText().let {
            authFlow(
                flowId = Json.parseToJsonElement(it)
                    .jsonObject["flow_id"]
                    .toString()
                    .replace("\"", ""),
                loginFlowRequestBodyWithId(login, password)
            )
        }
    }

    private suspend fun authFlow(flowId: String, loginFlowBodyWithId: LoginFlowRequestBodyWithId) {
        client.post("${serverSelectionRepository.getCurrentBaseUrl()}/auth/login_flow/$flowId") {
            contentType(ContentType.Application.Json)
            setBody(loginFlowBodyWithId)
        }.bodyAsText().let {
            token(
                code = Json.parseToJsonElement(it)
                    .jsonObject["result"]
                    .toString()
                    .replace("\"", ""),
            )
        }
    }

    private suspend fun token(code: String) {
        val requestBody = Parameters.build {
            append("grant_type", "authorization_code")
            append("code", code)
            append("client_id", "https://home-assistant.io/android")
        }.formUrlEncode()

        client.post("${serverSelectionRepository.getCurrentBaseUrl()}/auth/token") {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(requestBody)
        }.bodyAsText().let {
            // TODO Check code validity
            val json = Json.parseToJsonElement(it).jsonObject
            val accessToken = json["access_token"]?.jsonPrimitive?.content ?: ""
            val refreshToken = json["refresh_token"]?.jsonPrimitive?.content ?: ""
            val expiresInSeconds = json["expires_in"]?.jsonPrimitive?.intOrNull

            authTokensStore.saveTokens(
                dataStore = dataStore,
                accessToken = accessToken,
                refreshToken = refreshToken,
                expiresInSeconds = expiresInSeconds
            )

            socketClient.setToken(accessToken).connect()
        }
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
            val response =
                client.request("${serverSelectionRepository.getCurrentBaseUrl()}/api/config") {
                    method = HttpMethod("GET")
                    header(HttpHeaders.Authorization, "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(requestBody)
                }
            println(response.bodyAsText())
        } catch (e: Exception) {
            e.printStackTrace()
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

        client.post("${serverSelectionRepository.getCurrentBaseUrl()}/api/mobile_app/registrations") {
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }
    }

    override suspend fun getToken(): String? {
        return dataStore.data.map { preferences ->
            preferences[authTokensStore.KEY_ACCESS]
        }.first()
    }

    override suspend fun subscribeToken(): Flow<String> = dataStore.data.mapNotNull { preferences ->
        preferences[authTokensStore.KEY_ACCESS]
    }
}