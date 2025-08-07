package ru.bratusev.smartlab.domain.core.usecase

import kotlinx.coroutines.flow.Flow
import ru.bratusev.smartlab.domain.core.repository.SocketRepository

class ObserveSocketErrorsUseCase(
    private val socketRepository: SocketRepository
) {
    operator fun invoke(): Flow<List<Error>> = socketRepository.observeSocketErrors()
}


