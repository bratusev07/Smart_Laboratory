package ru.bratusev.smartlab.feature_customScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import ru.bratusev.smartlab.feature_customScreen.models.Event
import ru.bratusev.smartlab.feature_customScreen.models.Event.ChosenManySwitchesChange
import ru.bratusev.smartlab.feature_customScreen.models.Event.ChosenSingleSwitchChange
import ru.bratusev.smartlab.feature_customScreen.models.Event.DeleteWidget
import ru.bratusev.smartlab.feature_customScreen.models.Event.OnSensorStateChanged
import ru.bratusev.smartlab.ui.core.components.CustomWidget
import ru.bratusev.smartlab.ui.core.models.CustomWidgetEvent
import ru.bratusev.smartlab.ui.core.models.CustomWidgetUi
import ru.bratusev.smartlab.ui.core.resources.StringsRes
import ru.bratusev.smartlab.ui.core.theme.AppTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomScreen(
    vm: CustomScreenViewModel = koinViewModel(),
    setMenuAction: (action: () -> Unit) -> Unit,
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

    DisposableEffect(Unit) {
        setMenuAction {
            vm.handleEvent(Event.ToggleDropDownMenu)
        }

        onDispose {
            setMenuAction { }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 16.dp, start = 12.dp, end = 12.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(state.value.widgetsUi) {
                CustomWidget(
                    uiData = it.copy(isEditMode = state.value.isEditMode),
                    onEvent = { event ->
                        vm.handleEvent(getVmEvent(it, event))
                    })
            }
        }
        Column(
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 48.dp),
        ) {
            AnimatedVisibility(
                visible = state.value.isUpdating || state.value.isSaving
            ) {
                Column {
                    CircularProgressIndicator()
                    Text(if (state.value.isUpdating) StringsRes.UPDATING else StringsRes.SAVING)
                }
            }
        }


        // TODO: надо как-нибудь бы переделать эту штуку, да чтобы не надо было делать на каждом экране разное
        Box(modifier = Modifier.align(Alignment.TopEnd)) {
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
                text = { Text(StringsRes.ADD_WIDGET) },
                onClick = onAddScreen,
                leadingIcon = {
                    Icon(
                        Icons.Default.Add, contentDescription = null
                    )
                })
            DropdownMenuItem(
                text = { Text(StringsRes.EDIT_MODE) },
                onClick = onEditMode,
                leadingIcon = {
                    Icon(
                        Icons.Default.Edit, contentDescription = null
                    )
                })
        })
}

// Криво, но работает
@Preview(
    showBackground = true, heightDp = 900
)
@Composable
private fun MenuDropDownPreview() {
    AppTheme {
        MenuDropDown(
            isExpanded = true, onAddScreen = {}, onClose = {},
            onEditMode = {}
        )
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