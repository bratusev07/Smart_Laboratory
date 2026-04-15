package ru.bratusev.smartlab.domain.core.usecase

import kotlinx.coroutines.flow.Flow
import ru.bratusev.smartlab.domain.core.model.ServerSelection
import ru.bratusev.smartlab.domain.core.repository.ServerSelectionRepository

class GetServerSelectionFlowUseCase(
    private val serverSelectionRepository: ServerSelectionRepository
) {
    operator fun invoke(): Flow<ServerSelection> =
        serverSelectionRepository.observerServerSelection()
}

