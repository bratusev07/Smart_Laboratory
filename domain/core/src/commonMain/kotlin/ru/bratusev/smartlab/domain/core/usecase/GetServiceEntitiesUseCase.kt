package ru.bratusev.smartlab.domain.core.usecase

import kotlinx.coroutines.flow.Flow
import ru.bratusev.smartlab.domain.core.model.socket.ServiceEntity
import ru.bratusev.smartlab.domain.core.repository.AutomationRepository
import ru.bratusev.smartlab.domain.core.repository.SocketRepository

class GetServiceEntitiesUseCase(
    private val socketRepository: SocketRepository,
    private val automationRepository: AutomationRepository
) {
    suspend fun invoke(): Flow<List<ServiceEntity>> {
        socketRepository.fetchAutomation().let {
            automationRepository.fetchAutomaton(it)
        }
        return socketRepository.observeServiceEntities()
    }
}

