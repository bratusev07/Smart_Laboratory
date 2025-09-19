package ru.bratusev.smartlab.feature_areas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.bratusev.smartlab.domain.core.usecase.GetLoggerUseCase
import ru.bratusev.smartlab.feature_areas.models.AreasScreenState
import ru.bratusev.smartlab.feature_areas.models.Event

class AreasScreenViewModel(
    private val logger: GetLoggerUseCase,
    ) : ViewModel() {

    private val _uiState = MutableStateFlow(AreasScreenState())
    val uiState: StateFlow<AreasScreenState> = _uiState

    private fun updateState(updatedState: AreasScreenState) {
        viewModelScope.launch {
            _uiState.emit(updatedState)
        }
    }

    fun handleEvent(event: Event) {
        when (event) {
            Event.ToggleDropDownMenu -> {}
        }
    }
}
