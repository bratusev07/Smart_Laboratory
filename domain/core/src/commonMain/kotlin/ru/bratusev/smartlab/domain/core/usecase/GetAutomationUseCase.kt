package ru.bratusev.smartlab.domain.core.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.bratusev.smartlab.domain.core.model.automation.Automation
import ru.bratusev.smartlab.domain.core.repository.AutomationRepository
import ru.bratusev.smartlab.domain.core.repository.SocketRepository

class GetAutomationUseCase(
    private val socketRepository: SocketRepository,
    private val automationRepository: AutomationRepository
) {

    operator fun invoke(): Flow<Result<List<Automation>>> = flow {
        socketRepository.fetchAutomation().let {
            try {
                val automation = automationRepository.fetchAutomaton(it)
                emit(Result.success(automation))
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }
    }
}