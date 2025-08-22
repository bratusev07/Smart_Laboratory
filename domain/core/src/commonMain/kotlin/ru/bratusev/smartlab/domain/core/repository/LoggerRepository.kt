package ru.bratusev.smartlab.domain.core.repository

import kotlinx.coroutines.flow.Flow
import ru.bratusev.smartlab.domain.core.model.LogcatMessage

interface LoggerRepository {
    fun d(tag: String?, description: String)
    fun w(tag: String?, description: String)
    fun e(tag: String?, description: String)

    suspend fun getLogMessages(logTypes: List<String>) : Flow<List<LogcatMessage>>
}