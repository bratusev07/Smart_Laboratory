package ru.bratusev.smartlab.domain.core.repository

interface AutomationRepository {

    fun saveAutomation()

    fun fetchAutomaton(url: String)
}