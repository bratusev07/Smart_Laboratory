package ru.bratusev.smartlab.feature_area

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
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

    private val _areaId = MutableStateFlow<String?>(null)
    private var getAreaDevicesJob: Job? = null

    init {
        _areaId.filterNotNull().onEach { areaId ->
            loadData(areaId)
        }.launchIn(viewModelScope)
    }

    private fun loadData(areaId: String) {
        logger.d("AreaScreenViewModel/loadData", "Loading data for areaId: $areaId")
        getAreaDevicesJob?.cancel()
        updateState(_uiState.value.copy(isLoading = true, errorMessage = null, areaId = areaId))

        getAreaDevicesJob = getAreaDeviceUseCase.invoke(areaId)
            .onEach {
                logger.d("AreaScreenViewModel/getAreaDeviceUseCase", "Got devices: $it")
                updateState(
                    _uiState.value.copy(
                        areaDevices = it.map { service -> service.mapToUi() },
                        isLoading = false,
                        errorMessage = null
                    )
                )
            }
            .catch { e ->
                logger.e(
                    "AreaScreenViewModel/getAreaDeviceUseCase",
                    "Error getting devices: ${e.message}"
                )
                updateState(
                    _uiState.value.copy(
                        errorMessage = "Error loading devices: ${e.message}",
                        isLoading = false
                    )
                )
            }.launchIn(viewModelScope)
    }

    private fun updateState(updatedState: AreaScreenState) {
        viewModelScope.launch {
            _uiState.emit(updatedState)
        }
    }

    private fun onLoadingTimeOut() {
        updateState(_uiState.value.copy(isLoading = false))
    }

    fun handleEvent(event: Event) {
        when (event) {
            Event.ToggleDropDownMenu -> {}
            is Event.FetchData -> {
                _areaId.value = event.areaId
            }

            Event.OnLoadingTimeOut -> onLoadingTimeOut()
        }
    }
}