package ru.bratusev.smartlab.feature_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.bratusev.smartlab.domain.core.model.socket.ServiceEntity
import ru.bratusev.smartlab.domain.core.usecase.GetLoggerUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetServiceEntitiesUseCase
import ru.bratusev.smartlab.domain.core.usecase.ObserveSocketErrorsUseCase
import ru.bratusev.smartlab.domain.core.usecase.UpdateSensorUseCase
import ru.bratusev.smartlab.feature_home.models.Event
import ru.bratusev.smartlab.feature_home.models.HomeState

class HomeViewModel(
    private val updateSensorUseCase: UpdateSensorUseCase,
    private val logger: GetLoggerUseCase,
    getServiceEntitiesUseCase: GetServiceEntitiesUseCase,
    observeSocketErrorsUseCase: ObserveSocketErrorsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState

    init {
        viewModelScope.launch {
            getServiceEntitiesUseCase.invoke().collect {
                onServiceEntitiesUpdated(it)
            }
        }

        observeSocketErrorsUseCase.invoke().onEach { errors ->
            if (errors.isNotEmpty()) {
                val messages = errors.map { it.message ?: it.toString() }
                updateState(uiState.value.copy(socketErrors = messages))
            }
        }.launchIn(viewModelScope)
    }

    private fun onServiceEntitiesUpdated(entities: List<ServiceEntity>) {
        logger.d("HomeViewModel/onServiceEntitiesUpdated", "Got entities update")
        val switches: List<ServiceEntity> = entities
        updateState(uiState.value.copy(serviceEntities = switches, isUpdating = false))
    }

    private fun onCustomButtonClicked() {
        val state = uiState.value.copy(screenName = "New screen name")
        updateState(state)
    }

    private fun onButtonTextUpdated(text: String) {
        val state = uiState.value.copy(screenName = text)
        updateState(state)
    }

    private fun onSwitchUpdated(switchId: String) {
        updateState(_uiState.value.copy(isUpdating = true))
        uiState.value.serviceEntities.find { it.id == switchId }?.let {
            it.id?.let { id ->
                updateSensorUseCase.invoke(id).onEach { result ->
                    result.fold(onSuccess = {
                        logger.d("HomeViewModel switch switch", "Switched switch with id: $id")
                    }, onFailure = { error ->
                        logger.e(
                            "HomeViewModel switch switch", "Could switch with error: $error"
                        )
                    })
                }.launchIn(viewModelScope)
            }
        }
    }

    private fun updateState(updatedState: HomeState) {
        viewModelScope.launch {
            _uiState.emit(updatedState)
        }
    }

    private fun onUpdateTimeOut() {
        updateState(_uiState.value.copy(isUpdating = false))
    }

    internal fun handleEvent(event: Event) {
        when (event) {
            Event.OnBackClicked -> Unit
            Event.OnCustomButtonClicked -> onCustomButtonClicked()
            Event.OnUpdateTimeOut -> onUpdateTimeOut()
            is Event.OnButtonTextUpdated -> onButtonTextUpdated(event.text)
            is Event.OnSwitchUpdated -> onSwitchUpdated(event.switchId)
        }
    }
}
