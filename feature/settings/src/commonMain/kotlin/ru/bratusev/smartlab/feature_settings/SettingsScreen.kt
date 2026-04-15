package ru.bratusev.smartlab.feature_settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ru.bratusev.smartlab.feature_settings.models.Event
import ru.bratusev.smartlab.navigation.api.NavigationApi
import ru.bratusev.smartlab.ui.core.components.LoadingIndicator
import ru.bratusev.smartlab.ui.core.components.SettingsDropDown
import ru.bratusev.smartlab.ui.core.models.SettingsDropDownUi
import smartlaboratory.ui.core.generated.resources.Res
import smartlaboratory.ui.core.generated.resources.confirm_changes
import smartlaboratory.ui.core.generated.resources.language
import smartlaboratory.ui.core.generated.resources.saving
import smartlaboratory.ui.core.generated.resources.theme

@Composable
fun SettingsScreen(
    vm: SettingsViewModel = koinViewModel(),
    navigationApi: NavigationApi
) {
    val state = vm.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
            OutlinedButton(
                enabled = state.value.isChanged,
                onClick = { vm.handleEvent(Event.Confirm) },
                content = {
                    Text(text = stringResource(Res.string.confirm_changes))
                }
            )
            SettingsDropDown(
                settingsDropDownUi = SettingsDropDownUi(
                    label = stringResource(Res.string.language),
                    values = state.value.languages,
                    currentValue = state.value.newSettings.language.localNameRes,
                    originalValue = state.value.oldSettings.language.localNameRes
                ),
                onValueChange = { vm.handleEvent(Event.ChangeLanguage(it)) }
            )
            SettingsDropDown(
                settingsDropDownUi = SettingsDropDownUi(
                    label = stringResource(Res.string.theme),
                    values = state.value.themes,
                    currentValue = state.value.newSettings.theme.localeNameRes,
                    originalValue = state.value.oldSettings.theme.localeNameRes

                ),
                onValueChange = { vm.handleEvent(Event.ChangeTheme(it)) }
            )
        }
    }
    LoadingIndicator(
        state.value.isLoading,
        onTimeOut = { vm.handleEvent(Event.OnTimeOut) }
    )
    LoadingIndicator(
        state.value.isSaving, text = stringResource(Res.string.saving),
        onTimeOut = { vm.handleEvent(Event.OnTimeOut) }
    )
}

