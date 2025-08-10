package ru.bratusev.smartlab.data.core.repository.preview

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import io.ktor.client.HttpClient
import kotlinx.serialization.Serializable
import ru.bratusev.smartlab.domain.core.model.Device
import ru.bratusev.smartlab.domain.core.repository.AuthRepository

class AuthRepositoryPreview(
    private val client: HttpClient,
    private val dataStore: DataStore<Preferences>
) : AuthRepository {

    private val BASE_URL = "http://10.131.170.254:8123/preview"

    override suspend fun login(login: String, password: String): String {
        println("⚠️ Preview: login started 1/4")
        return auth(login, password)
    }

    private suspend fun providers() {
    }

    private suspend fun auth(login: String, password: String): String {
        println("⚠️ Preview: login auth method 2/4")
        return authFlow(
            flowId = "previewFlowId",
            login = login,
            password = password,
        )
    }

    private suspend fun authFlow(flowId: String, login: String, password: String): String {
        println("⚠️ Preview: login authFlow 3/4")
        return token("previewToken")
    }

    private suspend fun token(code: String): String {
        println("✅ Preview: login reached token 4/4")
        return "somePreviewToken"
    }

    // To Auth interface
    override suspend fun config(token: String) {}

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
    }

    override suspend fun saveToken(token: String) {
    }

    override suspend fun getToken(): String? {
        return "somePreviewToken"
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