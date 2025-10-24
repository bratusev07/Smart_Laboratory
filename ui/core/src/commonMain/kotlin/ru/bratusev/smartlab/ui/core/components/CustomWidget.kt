package ru.bratusev.smartlab.ui.core.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
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
        modifier = Modifier.fillMaxWidth().clip(shape = RoundedCornerShape(30.dp)).background(
            color = MaterialTheme.colorScheme.surfaceContainerLow
        ), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val focusManager = LocalFocusManager.current
        var isDeleteDialogOpen by rememberSaveable { mutableStateOf(false) }
        var isEditButtonClicked by rememberSaveable { mutableStateOf(false) }
        AnimatedVisibility(visible = uiData.isEditMode) {
            WidgetToolBar(
                title = "Виджет с id: ${uiData.id}",
                onEditClick = { isEditButtonClicked = true },
                onAddClick = {},
                isEditMode = uiData.isEditMode,
                onDeleteClick = { isDeleteDialogOpen = true })
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
        Spacer(
            modifier = Modifier.fillMaxWidth().height(12.dp).background(
                MaterialTheme.colorScheme.surfaceContainer,
            )
        )
        var title by rememberSaveable { mutableStateOf(uiData.title) }
        if (title != null) {
            BasicTextField(
                modifier = Modifier.fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainer).height(32.dp),
                enabled = uiData.isEditMode,
                value = title!!,
                keyboardActions = KeyboardActions(onDone = {
                    onEvent(CustomWidgetEvent.EditTitle(title!!))
                    focusManager.clearFocus()
                }),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                onValueChange = { if (it.length < 20) title = it else it.slice(0..19) },
                maxLines = 1,
                singleLine = true,
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    textDecoration = if (uiData.isEditMode) TextDecoration.Underline else null
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface)
            )
        }
        Spacer(
            modifier = Modifier.fillMaxWidth().height(12.dp).background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surfaceContainer,
                        MaterialTheme.colorScheme.surfaceContainerLow
                    )
                )
            )
        )
        Box(modifier = Modifier.padding(12.dp)) {
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
                    onEditEnd = { isEditButtonClicked = false })

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
                    onEditEnd = { isEditButtonClicked = false })
            }
        }
        Spacer(
            modifier = Modifier.fillMaxWidth().height(12.dp).background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surfaceContainerLow,
                        MaterialTheme.colorScheme.surfaceContainer
                    )
                )
            )
        )
        Spacer(
            modifier = Modifier.fillMaxWidth().height(24.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer)
        )
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
                sensorsToShow = data,
                id = 1,
                sensorsToChooseFrom = data2,
                openModal = false,
                title = "Preview title Preview title"
            ), onEvent = {})
    }
}
