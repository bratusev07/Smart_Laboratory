package ru.bratusev.smartlab.domain.core.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.bratusev.smartlab.domain.core.model.CustomWidget
import ru.bratusev.smartlab.domain.core.repository.WidgetsRepository

class GetCustomWidgetsUseCase(private val widgetsRepository: WidgetsRepository) {
    operator fun invoke(): Flow<Result<List<CustomWidget>>> = flow {
        try {
            emit(Result.success(widgetsRepository.getWidgets()))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}