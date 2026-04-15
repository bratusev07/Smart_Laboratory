package ru.bratusev.smartlab.domain.core.usecase

import ru.bratusev.smartlab.domain.core.model.ServerSelection
import ru.bratusev.smartlab.domain.core.repository.ServerSelectionRepository

class SetServerSelectionUseCase(
    private val serverSelectionRepository: ServerSelectionRepository
) {
    suspend operator fun invoke(serverSelection: ServerSelection) =
        serverSelectionRepository.setServerSelection(serverSelection)
}
