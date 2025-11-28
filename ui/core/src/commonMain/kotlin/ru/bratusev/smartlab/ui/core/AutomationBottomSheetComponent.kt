package ru.bratusev.smartlab.ui.core

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.bratusev.smartlab.ui.core.models.AutomationItemUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutomationBottomSheet(
    item: AutomationItemUi,
    sheetState: SheetState,
    onClose: () -> Unit,
    onDelete: (AutomationItemUi) -> Unit,
    onSave: (AutomationItemUi) -> Unit
) {
    var isEditMode by remember { mutableStateOf(false) }

    var alias by remember { mutableStateOf(item.alias) }
    var description by remember { mutableStateOf(item.description) }

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (isEditMode) {
                    TextButton(
                        onClick = {
                            isEditMode = false
                            onSave(
                                item.copy(
                                    alias = alias,
                                    description = description
                                )
                            )
                        }
                    ) {
                        Text("Сохранить")
                    }
                } else {
                    TextButton(onClick = { isEditMode = true }) {
                        Text("Изменить")
                    }

                    Spacer(Modifier.width(10.dp))

                    TextButton(
                        onClick = { onDelete(item) }
                    ) {
                        Text("Удалить", color = Color.Red)
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = item.id,
                onValueChange = {},
                label = { Text("ID") },
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = alias,
                onValueChange = { alias = it },
                label = { Text("Псевдоним") },
                enabled = isEditMode,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Описание") },
                enabled = isEditMode,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(40.dp))
        }
    }
}
