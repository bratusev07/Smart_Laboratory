package ru.bratusev.smartlab.feature_addWidgetScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import ru.bratusev.smartlab.domain.core.model.CustomWidget
import ru.bratusev.smartlab.feature_addWidgetScreen.models.Event
import ru.bratusev.smartlab.ui.core.components.AppTopBar
import ru.bratusev.smartlab.ui.core.components.widgets.ManySensorsWidget
import ru.bratusev.smartlab.ui.core.components.widgets.SingleSensorWidget
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
                    }, title = "Переключатели списком"
                ) { ManySensorsListWidgetPreview() }
            }
            item {
                WidgetItem(
                    onAccept = {
                        vm.handleEvent(Event.OnSaveWidget(CustomWidget.SingleSensor::class))
                    }, title = "Большой переключатель"
                ) { SingleSensorWidgetPreview() }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WidgetItem(onAccept: () -> Unit, title: String, content: @Composable () -> Unit) {
    var isDialogOpen by remember { mutableStateOf(false) }
    if (isDialogOpen) {
        Box(modifier = Modifier.fillMaxSize()) {
            AlertDialog(title = {
                Text(text = "Подтвердить выбор")
            }, onDismissRequest = {
                isDialogOpen = false
            }, confirmButton = {
                TextButton(
                    onClick = {
                        onAccept()
                        isDialogOpen = false
                    }) {
                    Text("Подтвердить")
                }
            }, dismissButton = {
                TextButton(
                    onClick = {
                        isDialogOpen = false
                    }) {
                    Text("Отменить")
                }
            })
        }
    }
    Surface(onClick = { isDialogOpen = true }) {
        Box {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                HorizontalDivider(
                    thickness = 4.dp,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(horizontal = 12.dp).clip(RoundedCornerShape(50))
                )
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                content()
            }
            Box(
                modifier = Modifier.matchParentSize().pointerInput(Unit) {})
        }
    }
}

@Composable
private fun SingleSensorWidgetPreview() {
    val sensor = SensorCardUi.Widget.Switch(
        title = "title",
        id = "id",
        state = SensorState.On,
        domain = SensorDomain.SWITCH,
        drawableResource = SensorCardRes.lightBulb,
        tints = SensorCardTints.Common.LightBulb
    )
    val data2 = buildList {
        for (i in 1..5) {
            add(
                SensorCardUi.Modal(
                    title = "Preview$i",
                    id = "Id$i",
                    state = SensorState.On,
                    domain = SensorDomain.SWITCH,
                    drawableResource = SensorCardRes.lightBulb,
                    tints = SensorCardTints.Common.LightBulb
                )
            )
        }
    }
    SingleSensorWidget(
        uiData = CustomWidgetUi.SingleSensor(
            sensor = sensor, sensorsToChooseFrom = data2, id = 1
        ), onToggle = { _, _ -> }, onSubmit = {}, onDeleteWidgetClick = {})
}

@Composable
private fun ManySensorsListWidgetPreview() {
    val data = buildList {
        for (i in 1..5) {
            add(
                SensorCardUi.Widget.Switch(
                    title = "Preview$i",
                    id = "Id$i",
                    state = SensorState.On,
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
                    state = SensorState.On,
                    domain = SensorDomain.SWITCH,
                    drawableResource = SensorCardRes.lightBulb,
                    tints = SensorCardTints.Common.LightBulb
                )
            )
        }
    }
    ManySensorsWidget(
        uiData = CustomWidgetUi.ManySensorsList(
            sensorsToShow = data, sensorsToChooseFrom = data2, id = 1,
        ), onToggle = { _, _ -> {} }, onSubmit = {}, onDeleteWidgetClick = {})
}
