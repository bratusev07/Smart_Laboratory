package ru.bratusev.smartlab.feature_customScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import ru.bratusev.smartlab.feature_customScreen.models.Event
import ru.bratusev.smartlab.navigation.api.NavigationApi
import ru.bratusev.smartlab.ui.core.components.CustomWidget
import ru.bratusev.smartlab.ui.core.models.CustomWidgetEvent

@Composable
fun CustomScreen(
    vm: CustomScreenViewModel = koinViewModel(),
    navigationApi: NavigationApi,
    setMenuAction: (action: () -> Unit) -> Unit,
) {
    val state = vm.uiState.collectAsState()

    // TODO: может что-то лучше можно придумать
    DisposableEffect(Unit) {
        setMenuAction {
            vm.handleEvent(Event.OnMenuButtonClicked)
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
        items(state.value.widgets) {
            CustomWidget(uiData = it, onEvent = { event ->
                vm.handleEvent(getVmEvent(it.id, event))
            })
        }
        item { Text("Is open ${state.value.isModalOpen}") }
    }
}

private fun getVmEvent(widgetId: Int, widgetEvent: CustomWidgetEvent): Event {
    return when (widgetEvent) {
        is CustomWidgetEvent.SensorStateChange -> Event.OnSensorStateChanged(
            widgetId, widgetEvent.sensorId, widgetEvent.newState
        )
    }
}