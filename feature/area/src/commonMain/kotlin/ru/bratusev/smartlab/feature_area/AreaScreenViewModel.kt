package ru.bratusev.smartlab.feature_area

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.bratusev.smartlab.domain.core.model.socket.Area
import ru.bratusev.smartlab.domain.core.usecase.GetAreaDevicesUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetAreasUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetLoggerUseCase
import ru.bratusev.smartlab.feature_area.mappers.toDomain
import ru.bratusev.smartlab.feature_area.models.AreaScreenState
import ru.bratusev.smartlab.feature_area.models.Event

class AreaScreenViewModel(
    getAreasUseCase: GetAreasUseCase,
    getAreaDeviceUseCase: GetAreaDevicesUseCase,
    private val logger: GetLoggerUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AreaScreenState())
    val uiState: StateFlow<AreaScreenState> = _uiState

    init {
        getAreasUseCase.invoke().onEach {
            onAreasUpdate(it)
        }.launchIn(viewModelScope)

        //TODO remove const area id
        getAreaDeviceUseCase.invoke("clabaratoriia_smarttech").onEach {
            logger.d("AreasScreenViewModel/OnAreaDevicesUpdated", it.size.toString())
        }.launchIn(viewModelScope)
    }

    private fun onAreasUpdate(domainAreas: List<Area>) {
        logger.d("AreasScreenViewModel/onAreasUpdate", "Loaded ares: $domainAreas")
        updateState(
            _uiState.value.copy(
            areas = domainAreas.map { it.toDomain() }
        ))
    }

    private fun updateState(updatedState: AreaScreenState) {
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
