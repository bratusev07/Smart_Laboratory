package ru.bratusev.smartlab.domain.core.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.bratusev.smartlab.domain.core.model.Device
import ru.bratusev.smartlab.domain.core.repository.AuthRepository

class GetLoginUseCase(private val authRepository: AuthRepository) {

    operator fun invoke(login: String, password: String, device: Device): Flow<Result<String>> = flow {
        try {
            val token = authRepository.login(login, password)
            emit(Result.success(token))

        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}