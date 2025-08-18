package ru.bratusev.smartlab.domain.core.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.bratusev.smartlab.domain.core.model.LogcatMessage
import ru.bratusev.smartlab.domain.core.repository.LoggerRepository

class GetLogcatMessagesUseCase(private val logger: LoggerRepository) {

    operator fun invoke(logTypes: List<String> = listOf<String>("d", "e", "w")): Flow<Result<List<LogcatMessage>>> = flow {
        try {
            val messages = logger.getLogMessages(logTypes)
            emit(Result.success(messages))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}