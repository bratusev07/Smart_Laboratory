package ru.bratusev.smartlab.feature_automation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel
import ru.bratusev.smartlab.feature_automation.models.Event
import ru.bratusev.smartlab.navigation.api.NavigationApi
import ru.bratusev.smartlab.ui.core.AutomationBottomSheet
import ru.bratusev.smartlab.ui.core.components.AutomationListComponent
import ru.bratusev.smartlab.ui.core.models.AutomationItemUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutomationScreen(
    vm: AutomationViewModel = koinViewModel(),
    navigationApi: NavigationApi
) {
    val state = vm.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

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
    }
}

