package ru.bratusev.smartlab.domain.core.usecase

import ru.bratusev.smartlab.domain.core.repository.LoggerRepository

class GetLoggerUseCase(private val logger: LoggerRepository) {
    fun d(tag: String?, description: String) {
        logger.d(tag, description)
    }

    fun w(tag: String?, description: String) {
        logger.w(tag, description)
    }

    fun e(tag: String?, description: String) {
        logger.e(tag, description)
    }
}