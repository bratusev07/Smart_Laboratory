package ru.bratusev.smartlab.feature_customScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import ru.bratusev.smartlab.feature_customScreen.models.Event
import ru.bratusev.smartlab.feature_customScreen.models.Event.OnSensorStateChanged
import ru.bratusev.smartlab.feature_customScreen.models.Event.OnSwitchesWidgetChanged
import ru.bratusev.smartlab.ui.core.components.CustomWidget
import ru.bratusev.smartlab.ui.core.models.CustomWidgetEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomScreen(
    vm: CustomScreenViewModel = koinViewModel(),
    setMenuAction: (action: () -> Unit) -> Unit,
    goToAddWidgetScreen: () -> Unit,
) {
    val state = vm.uiState.collectAsState()

    DisposableEffect(Unit) {
        setMenuAction {
            goToAddWidgetScreen()
        }

        onDispose {
            setMenuAction { }
        }
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
    }
}

private fun getVmEvent(widgetId: Int, widgetEvent: CustomWidgetEvent): Event {
    return when (widgetEvent) {
        is CustomWidgetEvent.SensorStateChange -> OnSensorStateChanged(
            widgetId, widgetEvent.sensorId, widgetEvent.newState
        )

        is CustomWidgetEvent.ChosenSwitchesChange -> OnSwitchesWidgetChanged(
            widgetId = widgetId,
            chosenIds = widgetEvent.chosenIds
        )
    }
}