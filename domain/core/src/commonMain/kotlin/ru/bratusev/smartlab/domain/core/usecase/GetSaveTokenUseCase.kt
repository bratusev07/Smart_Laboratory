package ru.bratusev.smartlab.domain.core.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.bratusev.smartlab.domain.core.repository.AuthRepository

class GetSaveTokenUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(token: String): Flow<Result<String>> =
        flow {
            try {
                authRepository.saveToken(token)
                emit(Result.success(token))
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }
}