package ru.bratusev.smartlab.feature_logcat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.bratusev.smartlab.domain.core.usecase.GetLogcatMessagesUseCase
import ru.bratusev.smartlab.feature_logcat.mappers.mapToUi
import ru.bratusev.smartlab.feature_logcat.models.Event
import ru.bratusev.smartlab.feature_logcat.models.LogcatState

class LogcatViewModel(
    getLogcatMessagesUseCase: GetLogcatMessagesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LogcatState())
    val uiState: StateFlow<LogcatState> = _uiState

    init {
        getLogcatMessagesUseCase.invoke(listOf("e", "d", "w")).onEach {
            it.fold(
                onSuccess = { updateState(uiState.value.copy(messages = it.map { it.mapToUi() })) },
                onFailure = {  }
            )
        }.launchIn(viewModelScope)
    }

    private fun updateState(updatedState: LogcatState) {
        viewModelScope.launch {
            _uiState.emit(updatedState)
        }
    }

    internal fun handleEvent(event: Event) {
        when(event) {
            Event.OnBackClicked -> Unit
        }
    }
}