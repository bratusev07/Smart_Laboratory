package ru.bratusev.smartlab.domain.core.repository

interface AutomationRepository {

    suspend fun saveAutomation()

    suspend fun fetchAutomaton(url: String)
}