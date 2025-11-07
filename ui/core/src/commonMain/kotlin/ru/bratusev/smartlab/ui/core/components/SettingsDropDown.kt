package ru.bratusev.smartlab.ui.core.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.ui.core.models.SettingsDropDownUi
import ru.bratusev.smartlab.ui.core.theme.AppTheme
import smartlaboratory.ui.core.generated.resources.Res
import smartlaboratory.ui.core.generated.resources.theme_system

@Composable
fun SettingsDropDown(
    modifier: Modifier = Modifier,
    settingsDropDownUi: SettingsDropDownUi,
    onValueChange: (StringResource) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("${settingsDropDownUi.label}: ")
        Box(modifier = modifier) {
            OutlinedButton(
                onClick = { expanded = !expanded },
                shape = MaterialTheme.shapes.small,
                colors = if (settingsDropDownUi.currentValue == settingsDropDownUi.originalValue) ButtonDefaults.outlinedButtonColors() else ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text(stringResource(settingsDropDownUi.currentValue))
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                settingsDropDownUi.values.forEach {
                    DropdownMenuItem(
                        text = { Text(stringResource(it)) },
                        onClick = {
                            onValueChange(it)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun SettingsDropDownPreview() {
    AppTheme {
        var settingValue by mutableStateOf(Res.string.theme_system)
        SettingsDropDown(
            settingsDropDownUi = SettingsDropDownUi(
                label = "Preview",
                values = emptyList(),
                currentValue = settingValue,
                originalValue = Res.string.theme_system
            ),
            onValueChange = {
                settingValue = it
            }
        )
    }
}