package ru.bratusev.smartlab.domain.core.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.bratusev.smartlab.domain.core.model.Device
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