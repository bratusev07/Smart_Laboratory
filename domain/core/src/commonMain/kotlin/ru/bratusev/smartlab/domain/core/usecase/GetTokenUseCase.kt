package ru.bratusev.smartlab.domain.core.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.bratusev.smartlab.domain.core.model.Device
import ru.bratusev.smartlab.domain.core.repository.AuthRepository

class GetTokenUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(): Flow<Result<String>> = flow {
        try {
            emit(
                Result.success(
                    authRepository.getToken() ?: ""
                )
            ) // TODO: продумать обработку отсутствия токена
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}