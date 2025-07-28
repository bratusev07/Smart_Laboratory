package ru.bratusev.smartlab.data.core

import io.ktor.client.HttpClient

expect class KtorClientFactory() {
    fun createClient(): HttpClient
}