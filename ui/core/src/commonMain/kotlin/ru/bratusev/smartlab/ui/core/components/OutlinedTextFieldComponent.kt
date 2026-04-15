package ru.bratusev.smartlab.ui.core.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import ru.bratusev.smartlab.ui.core.models.OutlinedTextFieldUi

@Composable
fun OutlinedTextFieldComponent(
    modifier: Modifier,
    outlinedTextFieldUi: OutlinedTextFieldUi,
) {
    var hidePassword by remember { mutableStateOf(true) }

    val visualTransformation = if (hidePassword && outlinedTextFieldUi.enableHidingPassword) {
        PasswordVisualTransformation()
    } else VisualTransformation.None

    OutlinedTextField(
        value = outlinedTextFieldUi.value,
        onValueChange = outlinedTextFieldUi.onValueChange,
        label = { Text(outlinedTextFieldUi.placeholder) },
        singleLine = outlinedTextFieldUi.singleLine,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        shape = outlinedTextFieldUi.shape ?: OutlinedTextFieldDefaults.shape,
        trailingIcon = {
            if (outlinedTextFieldUi.enableHidingPassword) {
                IconButton(
                    onClick = {
                        hidePassword = !hidePassword
                    }) {
                    Icon(
                        if (hidePassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null
                    )
                }
            }
        },
        modifier = modifier
    )
}