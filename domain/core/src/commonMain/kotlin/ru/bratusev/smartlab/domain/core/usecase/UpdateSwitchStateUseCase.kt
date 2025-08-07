package ru.bratusev.smartlab.domain.core.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.bratusev.smartlab.domain.core.repository.SocketRepository

class UpdateSwitchStateUseCase(private val socketRepository: SocketRepository) {

    operator fun invoke(switchId: String, switchState: String) : Flow<Result<Boolean>> = flow {
        try {
            val result = socketRepository.updateSwitch(switchId, switchState)
            emit(Result.success(result))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}