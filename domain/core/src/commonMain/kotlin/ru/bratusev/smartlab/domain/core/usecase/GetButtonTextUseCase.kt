package ru.bratusev.smartlab.domain.core.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.bratusev.smartlab.domain.core.repository.ButtonTextRepository

class GetButtonTextUseCase(private val buttonTextRepository: ButtonTextRepository) {

    operator fun invoke() : Flow<Result<String>> = flow {
        try {
            val text = buttonTextRepository.getText()
            emit(Result.success(text))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}