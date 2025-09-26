package ru.bratusev.smartlab.feature_areas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.bratusev.smartlab.domain.core.model.socket.Area
import ru.bratusev.smartlab.domain.core.usecase.GetAreasUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetLoggerUseCase
import ru.bratusev.smartlab.feature_areas.mappers.toDomain
import ru.bratusev.smartlab.feature_areas.models.AreasScreenState
import ru.bratusev.smartlab.feature_areas.models.Event

class AreasScreenViewModel(
    getAreasUseCase: GetAreasUseCase,
    private val logger: GetLoggerUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AreasScreenState())
    val uiState: StateFlow<AreasScreenState> = _uiState

    init {
        getAreasUseCase.invoke().onEach {
            onAreasUpdate(it)
        }.launchIn(viewModelScope)
    }

    private fun onAreasUpdate(domainAreas: List<Area>) {
        updateState(
            _uiState.value.copy(
            areas = domainAreas.map { it.toDomain() }
        ))
    }

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
