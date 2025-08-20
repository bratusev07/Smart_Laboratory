package ru.bratusev.smartlab.feature_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.bratusev.smartlab.domain.core.model.socket.ServiceEntity
import ru.bratusev.smartlab.domain.core.usecase.GetServiceEntitiesUseCase
import ru.bratusev.smartlab.domain.core.usecase.ObserveSocketErrorsUseCase
import ru.bratusev.smartlab.domain.core.usecase.UpdateSensorUseCase
import ru.bratusev.smartlab.feature_home.models.Event
import ru.bratusev.smartlab.feature_home.models.HomeState

class HomeViewModel(
    getServiceEntitiesUseCase: GetServiceEntitiesUseCase,
    private val updateSensorUseCase: UpdateSensorUseCase,
    observeSocketErrorsUseCase: ObserveSocketErrorsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState

    init {
        getServiceEntitiesUseCase.invoke().onEach {
            onServiceEntitiesUpdated(it)
        }.launchIn(viewModelScope)

        observeSocketErrorsUseCase.invoke().onEach { errors ->
            if (errors.isNotEmpty()) {
                val messages = errors.map { it.message ?: it.toString() }
                updateState(uiState.value.copy(socketErrors = messages))
            }
        }.launchIn(viewModelScope)
    }

    private fun onServiceEntitiesUpdated(entities: List<ServiceEntity>) {
        val switches: List<ServiceEntity> = entities
        updateState(uiState.value.copy(serviceEntities = switches))
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
        uiState.value.serviceEntities.find { it.id == switchId }?.let {
            it.id?.let { id ->
                updateSensorUseCase.invoke(id).onEach {  }.launchIn(viewModelScope)
            }
        }
    }

    private fun updateState(updatedState: HomeState) {
        viewModelScope.launch {
            _uiState.emit(updatedState)
        }
    }

    internal fun handleEvent(event: Event) {
        when (event) {
            Event.OnBackClicked -> Unit
            Event.OnCustomButtonClicked -> onCustomButtonClicked()
            is Event.OnButtonTextUpdated -> onButtonTextUpdated(event.text)
            is Event.OnSwitchUpdated -> onSwitchUpdated(event.switchId)
        }
    }
}
