package ru.bratusev.smartlab.ui.core.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.ui.core.components.widgets.ManySensorsWidget
import ru.bratusev.smartlab.ui.core.components.widgets.SingleSensorWidget
import ru.bratusev.smartlab.ui.core.models.CustomWidgetEvent
import ru.bratusev.smartlab.ui.core.models.CustomWidgetUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardRes
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardTints
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorCardUi
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorDomain
import ru.bratusev.smartlab.ui.core.models.sensorCard.SensorState
import ru.bratusev.smartlab.ui.core.theme.AppTheme

@Composable
fun CustomWidget(uiData: CustomWidgetUi, onEvent: (event: CustomWidgetEvent) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().background(
            shape = RoundedCornerShape(30.dp), color = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var isDeleteDialogOpen by rememberSaveable { mutableStateOf(false) }
        var isEditButtonClicked by rememberSaveable { mutableStateOf(false) }
        AnimatedVisibility(visible = uiData.isEditMode) {
            WidgetToolBar(
                title = "Виджет с id: ${uiData.id}",
                onEditClick = { isEditButtonClicked = true },
                onAddClick = {},
                isEditMode = uiData.isEditMode,
                onDeleteClick = { isDeleteDialogOpen = true }
            )
        }
        if (isDeleteDialogOpen) {
            Box(modifier = Modifier.fillMaxSize()) {
                AlertDialog(title = {
                    Text(text = "Подтвердите удаление")
                }, onDismissRequest = {
                    isDeleteDialogOpen = false
                }, confirmButton = {
                    TextButton(
                        onClick = {
                            onEvent(CustomWidgetEvent.DeleteWidget)
                            isDeleteDialogOpen = false
                        }) {
                        Text("Подтвердить")
                    }
                }, dismissButton = {
                    TextButton(
                        onClick = {
                            isDeleteDialogOpen = false
                        }) {
                        Text("Отменить")
                    }
                })
            }
        }
        var title by rememberSaveable { mutableStateOf(uiData.title) }
        if (title != null) {
            BasicTextField(
                readOnly = isEditButtonClicked,
                value = title!!,
                keyboardActions = KeyboardActions(onDone = {
                    onEvent(CustomWidgetEvent.EditTitle(title!!))
                }),
                onValueChange = { if (it.length < 30) title = it },
                maxLines = 1,
                textStyle = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onSurface),
            )
        }
        when (uiData) {
            is CustomWidgetUi.ManySensorsList -> ManySensorsWidget(
                uiData = uiData.copy(openModal = isEditButtonClicked),
                onToggle = { sensorId, newState ->
                    onEvent(CustomWidgetEvent.SensorStateChange(sensorId, newState))
                },
                onSubmit = { chosenIds ->
                    onEvent(
                        CustomWidgetEvent.ChosenManySwitchesChange(
                            chosenIds
                        )
                    )
                },
                onEditEnd = { isEditButtonClicked = false }
            )

            is CustomWidgetUi.SingleSensor -> SingleSensorWidget(
                uiData = uiData.copy(openModal = isEditButtonClicked),
                onToggle = { sensorId, newState ->
                    onEvent(CustomWidgetEvent.SensorStateChange(sensorId, newState))
                },
                onSubmit = { chosenId ->
                    onEvent(
                        CustomWidgetEvent.ChosenSingleSwitchChange(chosenId)
                    )
                },
                onEditEnd = { isEditButtonClicked = false }
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun SensorListPreview() {
    AppTheme {
        val data = buildList {
            for (i in 1..30) {
                add(
                    SensorCardUi.Widget.Switch(
                        title = "Preview$i",
                        id = "Id$i",
                        state = SensorState.On,
                        domain = SensorDomain.entries.random(),
                        drawableResource = SensorCardRes.lightBulb,
                        tints = SensorCardTints.Common.LightBulb
                    )
                )
            }
        }
        val data2 = buildList {
            for (i in 1..30) {
                add(
                    SensorCardUi.Modal(
                        title = "Preview$i",
                        id = "Id$i",
                        state = SensorState.On,
                        domain = SensorDomain.entries.random(),
                        drawableResource = SensorCardRes.lightBulb,
                        tints = SensorCardTints.Common.LightBulb
                    )
                )
            }
        }
        CustomWidget(
            uiData = CustomWidgetUi.ManySensorsList(
                sensorsToShow = data, id = 1, sensorsToChooseFrom = data2,
                openModal = false
            ), onEvent = {})
    }
}
