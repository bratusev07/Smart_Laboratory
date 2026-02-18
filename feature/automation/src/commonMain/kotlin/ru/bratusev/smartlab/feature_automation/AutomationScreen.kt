package ru.bratusev.smartlab.feature_automation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import ru.bratusev.smartlab.feature_automation.models.Event
import ru.bratusev.smartlab.navigation.api.NavigationApi
import ru.bratusev.smartlab.ui.core.AutomationBottomSheet
import ru.bratusev.smartlab.ui.core.components.AutomationListComponent
import ru.bratusev.smartlab.ui.core.components.AutomationSetupBottomSheet
import ru.bratusev.smartlab.ui.core.components.LoadingIndicator
import ru.bratusev.smartlab.ui.core.models.AutomationItemUi
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)
@Composable
fun AutomationScreen(
    vm: AutomationViewModel = koinViewModel(),
    navigationApi: NavigationApi
) {
    val state = vm.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var showAddAutomationSheet by remember { mutableStateOf(false) }

    LoadingIndicator(
        state.value.isAutomationLoading,
        onTimeOut = {vm.handleEvent(Event.OnTimeOut)}
    )

    if (showAddAutomationSheet && state.value.sensors.isNotEmpty()) {
        AutomationSetupBottomSheet(
            entities = state.value.sensors,
            onDismiss = { showAddAutomationSheet = false },
            onCreateAutomation = { automation ->
                val id = Uuid.random().toString()
                vm.handleEvent(Event.AddAutomation(automation.copy(id = id)))
            }
        )
    }

    var selectedItem by remember { mutableStateOf<AutomationItemUi?>(null) }
    var isSheetOpen by remember { mutableStateOf(false) }

    if (isSheetOpen && selectedItem != null) {
        AutomationBottomSheet(
            item = selectedItem!!,
            sheetState = sheetState,
            onClose = { isSheetOpen = false },
            onDelete = {
                vm.handleEvent(Event.OnDeleteAutomationClicked(it.id))
                isSheetOpen = false
            },
            onSave = { updated ->
                vm.handleEvent(Event.OnUpdateAutomationClicked(updated))
                selectedItem = updated
                isSheetOpen = false
            }
        )
    }

    Box(Modifier.fillMaxSize()) {
        AutomationListComponent(
            automationUi = state.value.automation
        ) { clickedId ->
            selectedItem =
                state.value.automation.automationList.first { it.id == clickedId }
            isSheetOpen = true
        }

        Row(modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 10.dp, end = 5.dp)) {
            FloatingActionButton(
                onClick = {vm.handleEvent(Event.OnSaveAutomation)}
            ) {
                Icon(Icons.Default.Save, contentDescription = "Save")
            }

            Spacer(modifier = Modifier.width(10.dp))

            FloatingActionButton(
                onClick = { showAddAutomationSheet = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    }
}

