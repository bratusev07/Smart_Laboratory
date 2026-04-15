package ru.bratusev.smartlab.data.core

import io.ktor.client.HttpClient
import ru.bratusev.smartlab.data.core.local_storage.dataStore.AuthTokensStore
import ru.bratusev.smartlab.domain.core.repository.ServerSelectionRepository

expect class KtorClientFactory(
    serverSelectionRepository: ServerSelectionRepository,
    authTokensStore: AuthTokensStore
) {
    fun createClient(): HttpClient
}