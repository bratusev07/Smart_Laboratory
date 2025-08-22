package ru.bratusev.smartlab.data.core.repository.preview

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import ru.bratusev.smartlab.data.core.preview.LoggerPreview
import ru.bratusev.smartlab.domain.core.model.LogcatMessage
import ru.bratusev.smartlab.domain.core.repository.LoggerRepository

class LoggerRepositoryPreview(private val logger: LoggerPreview) : LoggerRepository {
    override fun d(tag: String?, description: String) {
        logger.d(tag, description)
    }

    override fun w(tag: String?, description: String) {
        logger.d(tag, description)
    }

    override fun e(tag: String?, description: String) {
        logger.d(tag, description)
    }

    override suspend fun getLogMessages(logTypes: List<String>): Flow<List<LogcatMessage>> {
        TODO("Not yet implemented")
    }
}