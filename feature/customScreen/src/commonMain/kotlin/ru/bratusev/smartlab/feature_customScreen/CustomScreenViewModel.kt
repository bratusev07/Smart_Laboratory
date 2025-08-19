package ru.bratusev.smartlab.feature_customScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.bratusev.smartlab.feature_customScreen.models.CustomScreenState
import ru.bratusev.smartlab.feature_customScreen.models.Event

class CustomScreenViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow(CustomScreenState())
    val uiState: StateFlow<CustomScreenState> = _uiState

    private fun onCustomButtonClicked() {
        val state = uiState.value.copy(screenName = "New screen name")
        updateState(state)
    }

    private fun onButtonTextUpdated(text: String) {
        val state = uiState.value.copy(screenName = text)
        updateState(state)
    }

    private fun toggleModalMenu() {
        updateState(_uiState.value.copy(isModalOpen = !_uiState.value.isModalOpen))
    }

    private fun updateState(updatedState: CustomScreenState) {
        viewModelScope.launch {
            _uiState.emit(updatedState)
        }
    }


    internal fun handleEvent(event: Event) {
        when (event) {
            Event.OnBackClicked -> Unit
            Event.OnCustomButtonClicked -> onCustomButtonClicked()
            is Event.OnButtonTextUpdated -> onButtonTextUpdated(event.text)
            Event.OnMenuButtonClicked -> toggleModalMenu()
            Event.OnModalCloseClicked -> toggleModalMenu()
        }
    }
}