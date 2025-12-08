package ru.bratusev.smartlab.domain.core.usecase

import ru.bratusev.smartlab.domain.core.model.automation.Automation
import ru.bratusev.smartlab.domain.core.repository.AutomationRepository

class UpdateAutomationUseCase(
    private val automationRepository: AutomationRepository
) {

    suspend operator fun invoke(automations: List<Automation>) {
        val result = automationRepository.saveAutomation(automations)
        if(result.contains("success")) {
            // TODO Call configCheck and abort if result is bad
        } else {
            // TODO Make something if error response
        }
    }
}