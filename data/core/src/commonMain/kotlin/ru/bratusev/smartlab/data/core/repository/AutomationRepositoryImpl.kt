package ru.bratusev.smartlab.data.core.repository

import io.ktor.client.HttpClient
import ru.bratusev.smartlab.domain.core.repository.AutomationRepository

class AutomationRepositoryImpl(val client: HttpClient): AutomationRepository {

    override fun saveAutomation() {

    }

    override fun fetchAutomaton(url: String) {
        val updatedUrl = url
    }
}