package ru.bratusev.smartlab.feature_customScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import ru.bratusev.smartlab.feature_customScreen.models.Event
import ru.bratusev.smartlab.navigation.api.NavigationApi
import ru.bratusev.smartlab.ui.core.components.CustomWidget
import ru.bratusev.smartlab.ui.core.components.modals.FindSensorModal
import ru.bratusev.smartlab.ui.core.models.CustomWidgetEvent
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardRes
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorDomain
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomScreen(
    vm: CustomScreenViewModel = koinViewModel(),
    navigationApi: NavigationApi,
    setMenuAction: (action: () -> Unit) -> Unit,
) {
    val state = vm.uiState.collectAsState()
    var isModalOpen by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        setMenuAction {
            isModalOpen = true
        }

        onDispose {
            setMenuAction { }
        }
    }

    if (isModalOpen) {
        FindSensorModal(
            sensors = state.value.switchesEntities.map {
                SensorCardUi.Modal(
                    title = null,
                    id = it.id ?: "id",
                    state = SensorState.fromString(it.state),
                    domain = it.domain ?: "domain",
                    drawableResource = SensorCardRes.lightBulb,
                )
            },
            domain = SensorDomain.SWITCH,
            onClose = { isModalOpen = false }
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 16.dp, start = 12.dp, end = 12.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(state.value.widgetsUi) {
            CustomWidget(uiData = it, onEvent = { event ->
                vm.handleEvent(getVmEvent(it.id, event))
            })
        }
        item { Text("Is open $isModalOpen") }
    }
}

private fun getVmEvent(widgetId: Int, widgetEvent: CustomWidgetEvent): Event {
    return when (widgetEvent) {
        is CustomWidgetEvent.SensorStateChange -> Event.OnSensorStateChanged(
            widgetId, widgetEvent.sensorId, widgetEvent.newState
        )
    }
}