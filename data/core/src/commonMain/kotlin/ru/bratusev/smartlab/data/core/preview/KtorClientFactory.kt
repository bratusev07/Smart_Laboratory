package ru.bratusev.smartlab.data.core.preview

import io.ktor.client.HttpClient

expect class KtorClientFactoryPreview() {
    fun createClient(): HttpClient
}