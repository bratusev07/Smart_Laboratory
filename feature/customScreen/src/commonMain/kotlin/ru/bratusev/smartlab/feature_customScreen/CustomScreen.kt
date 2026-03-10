package ru.bratusev.smartlab.feature_customScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import ru.bratusev.smartlab.feature_customScreen.models.Event
import ru.bratusev.smartlab.feature_customScreen.models.Event.ChosenManySwitchesChange
import ru.bratusev.smartlab.feature_customScreen.models.Event.ChosenSingleSwitchChange
import ru.bratusev.smartlab.feature_customScreen.models.Event.DeleteWidget
import ru.bratusev.smartlab.feature_customScreen.models.Event.OnSensorStateChanged
import ru.bratusev.smartlab.ui.core.components.CustomWidget
import ru.bratusev.smartlab.ui.core.components.LoadingIndicator
import ru.bratusev.smartlab.ui.core.components.utils.RegisterTopBar
import ru.bratusev.smartlab.ui.core.models.CustomWidgetEvent
import ru.bratusev.smartlab.ui.core.models.CustomWidgetUi
import ru.bratusev.smartlab.ui.core.theme.AppTheme
import smartlaboratory.ui.core.generated.resources.Res
import smartlaboratory.ui.core.generated.resources.add_widget
import smartlaboratory.ui.core.generated.resources.edit_mode
import smartlaboratory.ui.core.generated.resources.empty_custom_screen_message
import smartlaboratory.ui.core.generated.resources.saving
import smartlaboratory.ui.core.generated.resources.updating

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomScreen(
    vm: CustomScreenViewModel = koinViewModel(),
    setTopBarComposable: (@Composable (RowScope.() -> Unit)) -> Unit,
    goToAddWidgetScreen: () -> Unit,
) {
    val state = vm.uiState.collectAsState()

    LaunchedEffect(Unit) {
        vm.handleEvent(Event.LoadData)
    }

    BackHandler(enabled = state.value.isEditMode || state.value.isSaving) {
        if (state.value.isEditMode) {
            vm.handleEvent(Event.ToggleEditMode)
        }
    }

    RegisterTopBar(setTopBarComposable) {
        Box {
            IconButton(onClick = { vm.handleEvent(Event.ToggleDropDownMenu) }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menu"
                )
            }

            MenuDropDown(
                isExpanded = state.value.isDropDownMenuExpanded,
                onClose = { vm.handleEvent(Event.ToggleDropDownMenu) },
                onAddScreen = {
                    goToAddWidgetScreen()
                    vm.handleEvent(Event.ToggleDropDownMenu)
                },
                onEditMode = {
                    vm.handleEvent(Event.ToggleEditMode)
                    vm.handleEvent(Event.ToggleDropDownMenu)
                },
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 16.dp, start = 12.dp, end = 12.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            if (state.value.widgetsUi.isEmpty()) {
                item {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(Res.string.empty_custom_screen_message),
                        textAlign = TextAlign.Center
                    )
                }
            } else items(state.value.widgetsUi) {
                CustomWidget(
                    uiData = it.copy(isEditMode = state.value.isEditMode), onEvent = { event ->
                        vm.handleEvent(getVmEvent(it, event))
                    })
            }
        }
        Column(
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 48.dp),
        ) {
            LoadingIndicator(
                state.value.isUpdating || state.value.isSaving,
                if (state.value.isUpdating) stringResource(Res.string.updating) else stringResource(
                    Res.string.saving
                ),
                onTimeOut = { vm.handleEvent(Event.OnTimeOut) }
            )
        }
    }
}

@Composable
private fun MenuDropDown(
    isExpanded: Boolean,
    onAddScreen: () -> Unit,
    onEditMode: () -> Unit,
    onClose: () -> Unit,
) {
    DropdownMenu(
        expanded = isExpanded, onDismissRequest = onClose, content = {
            DropdownMenuItem(
                text = { Text(stringResource(Res.string.add_widget)) },
                onClick = onAddScreen,
                leadingIcon = {
                    Icon(
                        Icons.Default.Add, contentDescription = null
                    )
                })
            DropdownMenuItem(
                text = { Text(stringResource(Res.string.edit_mode)) },
                onClick = onEditMode,
                leadingIcon = {
                    Icon(
                        Icons.Default.Edit, contentDescription = null
                    )
                })
        })
}

@Preview(
    showBackground = true, heightDp = 900
)
@Composable
private fun MenuDropDownPreview() {
    AppTheme {
        MenuDropDown(isExpanded = true, onAddScreen = {}, onClose = {}, onEditMode = {})
    }
}

private fun getVmEvent(widgetData: CustomWidgetUi, widgetEvent: CustomWidgetEvent): Event {
    return when (widgetEvent) {
        is CustomWidgetEvent.SensorStateChange -> OnSensorStateChanged(
            widgetData.id, widgetEvent.sensorId, widgetEvent.newState
        )

        is CustomWidgetEvent.ChosenManySwitchesChange -> ChosenManySwitchesChange(
            widgetId = widgetData.id,
            chosenIds = widgetEvent.chosenIds,
            title = widgetData.title ?: ""
        )

        CustomWidgetEvent.DeleteWidget -> DeleteWidget(
            widgetId = widgetData.id
        )

        is CustomWidgetEvent.ChosenSingleSwitchChange -> ChosenSingleSwitchChange(
            widgetId = widgetData.id,
            chosenId = widgetEvent.chosenId,
            title = widgetData.title ?: ""
        )

        is CustomWidgetEvent.EditTitle -> Event.EditTitle(
            widgetId = widgetData.id, title = widgetEvent.title
        )
    }
}