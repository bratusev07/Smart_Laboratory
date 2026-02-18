package ru.bratusev.smartlab.domain.core.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.bratusev.smartlab.domain.core.model.Device
import ru.bratusev.smartlab.domain.core.repository.AuthRepository

class GetLoginUseCase(private val authRepository: AuthRepository) {

    operator fun invoke(login: String, password: String, device: Device): Flow<Result<String>> =
        flow {
            try {
                authRepository.login(login, password)
                authRepository.subscribeToken().collect { token ->
                    if (token.isEmpty()) emit(Result.failure(Exception("authRepository returned empty token")))
                    else
                        emit(Result.success(token))
                }

            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }
}