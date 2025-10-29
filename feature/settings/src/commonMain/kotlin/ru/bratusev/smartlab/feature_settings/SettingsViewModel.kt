package ru.bratusev.smartlab.feature_settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.bratusev.smartlab.data.core.Logger
import ru.bratusev.smartlab.domain.core.usecase.GetSettingsUseCase
import ru.bratusev.smartlab.domain.core.usecase.UpdateSettingsUseCase
import ru.bratusev.smartlab.feature_settings.mappers.toDomain
import ru.bratusev.smartlab.feature_settings.mappers.toUi
import ru.bratusev.smartlab.feature_settings.models.Event
import ru.bratusev.smartlab.feature_settings.models.SettingsState
import ru.bratusev.smartlab.feature_settings.models.UiSettings

class SettingsViewModel(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val updateSettingsUseCase: UpdateSettingsUseCase,
    private val logger: Logger
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsState())
    val uiState: StateFlow<SettingsState> = _uiState

    init {
        getSettings()
    }

    private fun getSettings() {
        viewModelScope.launch {
            updateState(_uiState.value.copy(isLoading = true))
            val settings = getSettingsUseCase()?.toUi() ?: UiSettings()
            logger.d("SettingsViewModel/getSettings", "Loaded settings: $settings")
            updateState(_uiState.value.copy(oldSettings = settings))
            updateState(_uiState.value.copy(newSettings = settings))
            updateState(_uiState.value.copy(isLoading = false))
        }
    }

    private fun saveSettings() {
        viewModelScope.launch {
            updateState(_uiState.value.copy(isSaving = true))
            updateSettingsUseCase(_uiState.value.newSettings.toDomain())
            updateState(_uiState.value.copy(isSaving = false))
            getSettings()
        }
    }

    private fun changeLanguage(localeName: String) {
        val newLanguage = UiSettings.Language.fromLocaleName(localeName)
        logger.d("SettingsViewModel/changeLanguage", "Changed language to $localeName")
        updateSettings(
            newSettings = _uiState.value.newSettings.copy(
                language = newLanguage
            )
        )
    }

    private fun updateSettings(newSettings: UiSettings) {
        updateState(_uiState.value.copy(newSettings = newSettings))
    }

    private fun updateState(updatedState: SettingsState) {
        viewModelScope.launch {
            _uiState.emit(updatedState)
        }
    }

    internal fun handleEvent(event: Event) {
        when (event) {
            is Event.ChangeLanguage -> changeLanguage(event.localeName)
            is Event.Confirm -> saveSettings()
        }
    }
}