package ru.bratusev.smartlab.domain.core.usecase

import kotlinx.coroutines.flow.Flow
import ru.bratusev.smartlab.domain.core.model.socket.Area
import ru.bratusev.smartlab.domain.core.repository.SocketRepository

class GetAreasUseCase(
    private val socketRepository: SocketRepository
) {
    operator fun invoke(): Flow<List<Area>> = socketRepository.observeAreas()
}

