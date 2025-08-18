package ru.bratusev.smartlab.ui.core.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import ru.bratusev.smartlab.ui.core.models.OutlinedTextFieldUi

@Composable
fun OutlinedTextFieldComponent(modifier: Modifier, outlinedTextFieldUi: OutlinedTextFieldUi) {
    val visualTransformation = if (outlinedTextFieldUi.isSecret) {
        PasswordVisualTransformation()
    } else VisualTransformation.None

    OutlinedTextField(
        value = outlinedTextFieldUi.value,
        onValueChange = outlinedTextFieldUi.onValueChange,
        label = { Text(outlinedTextFieldUi.placeholder) },
        singleLine = outlinedTextFieldUi.singleLine,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        modifier = modifier
    )
}