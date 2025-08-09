package ru.bratusev.smartlab.data.core.repository

import ru.bratusev.smartlab.data.core.Logger
import ru.bratusev.smartlab.domain.core.repository.LoggerRepository

class LoggerRepositoryImpl(private val logger: Logger) : LoggerRepository {
    override fun d(tag: String?, description: String) {
        logger.d(tag, description)
    }

    override fun w(tag: String?, description: String) {
        logger.d(tag, description)
    }

    override fun e(tag: String?, description: String) {
        logger.d(tag, description)
    }
}