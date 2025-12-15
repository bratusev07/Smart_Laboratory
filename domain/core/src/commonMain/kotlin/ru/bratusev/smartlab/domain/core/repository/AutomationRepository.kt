package ru.bratusev.smartlab.domain.core.repository

import ru.bratusev.smartlab.domain.core.model.automation.Automation

interface AutomationRepository {

    suspend fun saveAutomation(automationData: List<Automation>): String

    suspend fun fetchAutomaton(url: String): List<Automation>
}