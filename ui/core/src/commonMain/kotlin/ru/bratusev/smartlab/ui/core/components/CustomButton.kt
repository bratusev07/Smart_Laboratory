package ru.bratusev.smartlab.ui.core.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.ui.core.models.CustomButtonUi
import ru.bratusev.smartlab.ui.core.theme.AppTheme

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    customButtonUi: CustomButtonUi,
    onCustomButtonClicked: () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = onCustomButtonClicked,
        enabled = customButtonUi.isEnabled
    ) {
        Text(
            text = customButtonUi.title,
            fontWeight = FontWeight(customButtonUi.fontWeight)
        )
    }
}

@Composable
@Preview(
    group = "CustomButton",
    name = "dark",
    showBackground = true,
)
private fun CustomButtonPreviewDark() {
    AppTheme(darkTheme = true) {
        CustomButton(
            customButtonUi = CustomButtonUi(
                title = "Preview",
                fontWeight = 200
            ),
            onCustomButtonClicked = {}
        )
    }
}

@Composable
@Preview(
    group = "CustomButton",
    name = "light",
    showBackground = true,
)
private fun CustomButtonPreviewLight() {
    AppTheme(darkTheme = false) {
        CustomButton(
            customButtonUi = CustomButtonUi(
                title = "Preview",
                fontWeight = 200
            ),
            onCustomButtonClicked = {}
        )
    }
}
