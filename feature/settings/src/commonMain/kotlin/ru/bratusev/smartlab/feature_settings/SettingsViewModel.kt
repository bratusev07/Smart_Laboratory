package ru.bratusev.smartlab.feature_settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import ru.bratusev.smartlab.domain.core.usecase.GetButtonTextUseCase
import ru.bratusev.smartlab.feature_settings.models.Event
import ru.bratusev.smartlab.feature_settings.models.SettingsState

class SettingsViewModel(
    getButtonTextUseCase: GetButtonTextUseCase
) : ViewModel(), KoinComponent {


    private val _uiState = MutableStateFlow(SettingsState())
    val uiState: StateFlow<SettingsState> = _uiState

    init {
        getButtonTextUseCase
            .invoke()
            .onEach {
                it.fold({ result ->
                    // handleEvent(Event.OnButtonTextUpdated(result))
                }) {
                    // TODO Обработка ошибки на UI
                }
            }
            .launchIn(viewModelScope)
    }

    private fun onCustomButtonClicked() {
        val state = uiState.value.copy(screenName = "New screen name")
        updateState(state)
    }

    private fun onButtonTextUpdated(text: String) {
        val state = uiState.value.copy(screenName = text)
        updateState(state)
    }

    private fun updateState(updatedState: SettingsState) {
        viewModelScope.launch {
            _uiState.emit(updatedState)
        }
    }

    internal fun handleEvent(event: Event) {
        when(event) {
            Event.OnBackClicked -> Unit
            Event.OnCustomButtonClicked -> onCustomButtonClicked()
            is Event.OnButtonTextUpdated -> onButtonTextUpdated(event.text)
        }
    }
}