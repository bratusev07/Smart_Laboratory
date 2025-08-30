package ru.bratusev.smartlab.feature_addWidgetScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import org.koin.compose.viewmodel.koinViewModel
import ru.bratusev.smartlab.domain.core.model.CustomWidget
import ru.bratusev.smartlab.feature_addWidgetScreen.models.Event
import ru.bratusev.smartlab.ui.core.components.AppTopBar
import ru.bratusev.smartlab.ui.core.components.widgets.SensorsWidget
import ru.bratusev.smartlab.ui.core.models.AppTopBarUi
import ru.bratusev.smartlab.ui.core.models.CustomWidgetUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardRes
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardTints
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorDomain
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWidgetScreen(
    vm: AddWidgetScreenViewModel = koinViewModel(),
    onGoBack: () -> Unit,
) {
    val state = vm.uiState.collectAsState()

    LaunchedEffect(state.value.isReadyToGoBack) {
        if (state.value.isReadyToGoBack) {
            onGoBack()
        }
    }

    Scaffold(topBar = {
        AppTopBar(
            uiData = AppTopBarUi(
                title = "Добавить виджет"
            ), onTitleClick = {}, onMenuClick = {})
    }) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item {
                WidgetItem(
                    onAccept = {
                        vm.handleEvent(Event.OnSaveWidget(CustomWidget.SensorsList::class))
                    }
                ) { SensorsListWidget() }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WidgetItem(onAccept: () -> Unit, content: @Composable () -> Unit) {
    var isDialogOpen by remember { mutableStateOf(false) }
    if (isDialogOpen) {
        Box(modifier = Modifier.fillMaxSize()) {
            AlertDialog(
                title = {
                    Text(text = "Подтвердить выбор")
                },
                onDismissRequest = {
                    isDialogOpen = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onAccept()
                            isDialogOpen = false
                        }
                    ) {
                        Text("Подтвердить")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            isDialogOpen = false
                        }
                    ) {
                        Text("Отменить")
                    }
                }
            )
        }
    }
    Surface(onClick = { isDialogOpen = true }) {
        Box {
            content()
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .pointerInput(Unit) {}
            )
        }
    }
}

@Composable
private fun SensorsListWidget() {
    val data = buildList {
        for (i in 1..5) {
            add(
                SensorCardUi.Widget.Switches(
                    title = "Preview$i",
                    id = "Id$i",
                    state = SensorState.entries[(0..2).random()],
                    domain = SensorDomain.SWITCH,
                    drawableResource = SensorCardRes.lightBulb,
                    tints = SensorCardTints.Common.LightBulb
                )
            )
        }
    }
    val data2 = buildList {
        for (i in 1..5) {
            add(
                SensorCardUi.Modal(
                    title = "Preview$i",
                    id = "Id$i",
                    state = SensorState.entries[(0..2).random()],
                    domain = SensorDomain.SWITCH,
                    drawableResource = SensorCardRes.lightBulb,
                    tints = SensorCardTints.Common.LightBulb
                )
            )
        }
    }
    SensorsWidget(
        uiData = CustomWidgetUi.SensorsList(
            sensorsToShow = data, sensorsToChooseFrom = data2, id = 1,
        ), onToggle = { _, _ -> {} }, onSubmit = {},
        onDeleteWidgetClick = {}
    )
}
