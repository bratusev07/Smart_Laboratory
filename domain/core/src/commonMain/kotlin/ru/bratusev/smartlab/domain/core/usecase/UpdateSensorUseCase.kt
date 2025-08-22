package ru.bratusev.smartlab.domain.core.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.bratusev.smartlab.domain.core.repository.SocketRepository

class UpdateSensorUseCase(private val socketRepository: SocketRepository) {

    operator fun invoke(sensorId: String) : Flow<Result<Boolean>> = flow {
        try {
            val result = socketRepository.updateSensor(sensorId)
            emit(Result.success(result))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}