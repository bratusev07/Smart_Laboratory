package ru.bratusev.smartlab.ui.core.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
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
import smartlaboratory.ui.core.generated.resources.Res
import smartlaboratory.ui.core.generated.resources.cancel
import smartlaboratory.ui.core.generated.resources.confirm
import smartlaboratory.ui.core.generated.resources.confirm_changes
import smartlaboratory.ui.core.generated.resources.widget_id_is

@Composable
fun CustomWidget(uiData: CustomWidgetUi, onEvent: (event: CustomWidgetEvent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(30.dp))
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerLow
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current
        var isDeleteDialogOpen by rememberSaveable { mutableStateOf(false) }
        var isEditButtonClicked by rememberSaveable { mutableStateOf(false) }
        var isAddButtonClicked by rememberSaveable { mutableStateOf(false) }

        AnimatedVisibility(visible = uiData.isEditMode) {
            WidgetToolBar(
                title = "${stringResource(Res.string.widget_id_is)} ${uiData.id}",
                onEditClick = { isEditButtonClicked = true },
                onAddClick = {
                    isEditButtonClicked = true
                    isAddButtonClicked = true
                },
                isEditMode = uiData.isEditMode,
                onDeleteClick = { isDeleteDialogOpen = true },
                showAddButton = uiData.showAddButton
            )
        }

        if (isDeleteDialogOpen) {
            AlertDialog(
                title = { Text(stringResource(Res.string.confirm_changes)) },
                onDismissRequest = { isDeleteDialogOpen = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onEvent(CustomWidgetEvent.DeleteWidget)
                            isDeleteDialogOpen = false
                        }
                    ) {
                        Text(stringResource(Res.string.confirm))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { isDeleteDialogOpen = false }
                    ) {
                        Text(stringResource(Res.string.cancel))
                    }
                }
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceContainer,
                )
        )

        var title by rememberSaveable { mutableStateOf(uiData.title) }

        LaunchedEffect(uiData.title) {
            if (title != uiData.title) {
                title = uiData.title
            }
        }

        if (title != null) {
            val onEditDone = {
                onEvent(CustomWidgetEvent.EditTitle(title!!))
                keyboardController?.hide()
                focusManager.clearFocus()
            }

            BasicTextField(
                modifier = Modifier
                    .onFocusChanged { focusState ->
                        if (!focusState.isFocused) {
                            onEvent(CustomWidgetEvent.EditTitle(title!!))
                        }
                    }
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .height(32.dp),
                enabled = uiData.isEditMode,
                value = title!!,
                keyboardActions = KeyboardActions(onDone = { onEditDone() }),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                onValueChange = {
                    title = it.take(20)
                },
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
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .background(
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
                    uiData = uiData.copy(
                        openModal = isEditButtonClicked,
                        isAddMode = isAddButtonClicked
                    ),
                    onToggle = { sensorId, newState ->
                        onEvent(CustomWidgetEvent.SensorStateChange(sensorId, newState))
                    },
                    onSubmit = { chosenIds ->
                        onEvent(CustomWidgetEvent.ChosenManySwitchesChange(chosenIds))
                    },
                    onEditEnd = {
                        isEditButtonClicked = false
                        isAddButtonClicked = false
                    }
                )

                is CustomWidgetUi.SingleSensor -> SingleSensorWidget(
                    uiData = uiData.copy(
                        openModal = isEditButtonClicked,
                        isAddMode = isAddButtonClicked
                    ),
                    onToggle = { sensorId, newState ->
                        onEvent(CustomWidgetEvent.SensorStateChange(sensorId, newState))
                    },
                    onSubmit = { chosenId ->
                        onEvent(CustomWidgetEvent.ChosenSingleSwitchChange(chosenId))
                    },
                    onEditEnd = {
                        isEditButtonClicked = false
                        isAddButtonClicked = false
                    }
                )
            }
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceContainerLow,
                            MaterialTheme.colorScheme.surfaceContainer
                        )
                    )
                )
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
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
            ),
            onEvent = {}
        )
    }
}