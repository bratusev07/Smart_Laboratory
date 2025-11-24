package ru.bratusev.smartlab.domain.core.repository

interface AutomationRepository {

    suspend fun saveAutomation(automationData: String)

    suspend fun fetchAutomaton(url: String)
}