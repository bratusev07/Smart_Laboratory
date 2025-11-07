package ru.bratusev.smartlab.feature_settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ru.bratusev.smartlab.feature_settings.models.Event
import ru.bratusev.smartlab.navigation.api.NavigationApi
import ru.bratusev.smartlab.ui.core.components.LoadingIndicator
import ru.bratusev.smartlab.ui.core.components.SettingsDropDown
import ru.bratusev.smartlab.ui.core.models.SettingsDropDownUi
import smartlaboratory.ui.core.generated.resources.Res
import smartlaboratory.ui.core.generated.resources.saving

@Composable
fun SettingsScreen(
    vm: SettingsViewModel = koinViewModel(),
    navigationApi: NavigationApi
) {
    val state = vm.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Button(
                enabled = state.value.isChanged,
                onClick = { vm.handleEvent(Event.Confirm) },
                content = {
                    Text(text = "Принять изменения")
                }
            )
            SettingsDropDown(
                settingsDropDownUi = SettingsDropDownUi(
                    values = state.value.languages,
                    currentValue = state.value.newSettings.language.localeName
                ),
                onValueChange = { vm.handleEvent(Event.ChangeLanguage(it)) }
            )
            SettingsDropDown(
                settingsDropDownUi = SettingsDropDownUi(
                    values = state.value.themes,
                    currentValue = state.value.newSettings.theme.localeName
                ),
                onValueChange = { vm.handleEvent(Event.ChangeTheme(it)) }
            )
            LoadingIndicator(state.value.isLoading)
            LoadingIndicator(state.value.isSaving, text = stringResource(Res.string.saving))
        }
    }
}

