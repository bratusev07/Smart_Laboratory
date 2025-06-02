package ru.bratusev.smartlab.ui.core.components

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import ru.bratusev.smartlab.ui.core.models.CustomButtonUi

@Composable
fun CustomButton(modifier: Modifier = Modifier, customButtonUi: CustomButtonUi, onCustomButtonClicked: () -> Unit) {
    Button(modifier = modifier, onClick = onCustomButtonClicked) {
        Text(
            text = customButtonUi.title,
            fontWeight = FontWeight(customButtonUi.fontWeight)
        )
    }
}