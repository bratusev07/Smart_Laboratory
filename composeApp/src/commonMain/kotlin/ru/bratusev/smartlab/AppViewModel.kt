package ru.bratusev.smartlab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.bratusev.smartlab.domain.core.usecase.GetSettingsUseCase
import ru.bratusev.smartlab.feature_settings.models.UiSettings

class AppViewModel(
    getSettingsUseCase: GetSettingsUseCase,
) : ViewModel() {
    val uiState: StateFlow<AppState> = getSettingsUseCase().map { domainSettings ->
            val isDarkTheme = when (domainSettings?.toUi()?.theme) {
                UiSettings.Theme.DARK -> true
                UiSettings.Theme.LIGHT -> false
                else -> null
            }
            AppState(
                isDarkTheme = isDarkTheme,
                isLoadingSettings = false,
                isoLangName = if (domainSettings?.isoLanguage != UiSettings.Language.SYSTEM.isoLanguage) domainSettings?.isoLanguage else null
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppState(isLoadingSettings = true)
        )
}

data class AppState(
    val isDarkTheme: Boolean? = null,
    val isLoadingSettings: Boolean = false,
    val isoLangName: String? = null
)
