package ru.bratusev.smartlab.feature_customScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import ru.bratusev.smartlab.feature_customScreen.models.Event
import ru.bratusev.smartlab.feature_customScreen.models.Event.OnSensorStateChanged
import ru.bratusev.smartlab.feature_customScreen.models.Event.OnSwitchesWidgetChanged
import ru.bratusev.smartlab.ui.core.components.CustomWidget
import ru.bratusev.smartlab.ui.core.models.CustomWidgetEvent
import ru.bratusev.smartlab.ui.core.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
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
                    uiData = it.copy(showDeleteButton = state.value.isDeleteMode),
                    onEvent = { event ->
                        vm.handleEvent(getVmEvent(it.id, event))
                    })
            }
        }

        // TODO: надо как-нибудь бы переделать эту штуку, да чтобы не надо было делать на каждом экране разное
        Box(modifier = Modifier.align(Alignment.TopEnd)) {
            MenuDropDown(
                isExpanded = state.value.isDropDownMenuExpanded,
                onClose = { vm.handleEvent(Event.ToggleDropDownMenu) },
                onAddWidget = {
                    goToAddWidgetScreen()
                    vm.handleEvent(Event.ToggleDropDownMenu)
                },
                onRemoveWidget = {
                    vm.handleEvent(Event.ToggleDeleteMode)
                    vm.handleEvent(Event.ToggleDropDownMenu)
                })
        }
    }
}

@Composable
private fun MenuDropDown(
    isExpanded: Boolean,
    onAddWidget: () -> Unit,
    onRemoveWidget: () -> Unit,
    onClose: () -> Unit,
) {
    DropdownMenu(
        expanded = isExpanded, onDismissRequest = onClose, content = {
            DropdownMenuItem(
                text = { Text("Добавить виджет") },
                onClick = onAddWidget,
                leadingIcon = {
                    Icon(
                        Icons.Default.Add, contentDescription = null
                    )
                })
            DropdownMenuItem(
                text = { Text("Удалить виджет") },
                onClick = onRemoveWidget,
                leadingIcon = {
                    Icon(
                        Icons.Default.Delete, contentDescription = null
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
        MenuDropDown(isExpanded = true, onAddWidget = {}, onRemoveWidget = {}, onClose = {})
    }
}

private fun getVmEvent(widgetId: Int, widgetEvent: CustomWidgetEvent): Event {
    return when (widgetEvent) {
        is CustomWidgetEvent.SensorStateChange -> OnSensorStateChanged(
            widgetId, widgetEvent.sensorId, widgetEvent.newState
        )

        is CustomWidgetEvent.ChosenSwitchesChange -> OnSwitchesWidgetChanged(
            widgetId = widgetId, chosenIds = widgetEvent.chosenIds
        )

        CustomWidgetEvent.DeleteWidget -> Event.DeleteWidget(
            widgetId = widgetId
        )
    }
}