package ru.bratusev.smartlab.feature_area

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.bratusev.smartlab.domain.core.usecase.GetAreaDevicesUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetLoggerUseCase
import ru.bratusev.smartlab.feature_area.mappers.mapToUi
import ru.bratusev.smartlab.feature_area.models.AreaScreenState
import ru.bratusev.smartlab.feature_area.models.Event

class AreaScreenViewModel(
    private val getAreaDeviceUseCase: GetAreaDevicesUseCase,
    private val logger: GetLoggerUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AreaScreenState())
    val uiState: StateFlow<AreaScreenState> = _uiState

    private fun loadAreaDevices(areaId: String) {
        getAreaDeviceUseCase.invoke(areaId).onEach {
            logger.d("AreasScreenViewModel/OnAreaDevicesUpdated", it.size.toString())
            uiState.value.copy(areaDevices = it.map { service -> service.mapToUi() }).let { state ->
                updateState(state)
            }
        }.launchIn(viewModelScope)
    }

    private fun updateState(updatedState: AreaScreenState) {
        viewModelScope.launch {
            _uiState.emit(updatedState)
        }
    }

    fun handleEvent(event: Event) {
        when (event) {
            Event.ToggleDropDownMenu -> {}
            is Event.LoadDevices -> loadAreaDevices(event.areaId)
        }
    }
}
