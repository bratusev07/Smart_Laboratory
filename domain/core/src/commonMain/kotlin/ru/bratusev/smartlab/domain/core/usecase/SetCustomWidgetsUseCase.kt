package ru.bratusev.smartlab.domain.core.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.bratusev.smartlab.domain.core.model.CustomWidget
import ru.bratusev.smartlab.domain.core.repository.WidgetsRepository

class SetCustomWidgetsUseCase(private val widgetsRepository: WidgetsRepository) {
    operator fun invoke(widgets: List<CustomWidget>): Flow<Result<Unit>> = flow {
        try {
            widgetsRepository.saveWidgets(widgets)
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}