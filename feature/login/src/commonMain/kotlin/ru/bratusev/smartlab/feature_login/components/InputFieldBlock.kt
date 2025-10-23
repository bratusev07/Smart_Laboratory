package ru.bratusev.smartlab.feature_login.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.feature_login.models.LoginState
import ru.bratusev.smartlab.ui.core.components.CustomButton
import ru.bratusev.smartlab.ui.core.components.OutlinedTextFieldComponent
import ru.bratusev.smartlab.ui.core.models.CustomButtonUi
import ru.bratusev.smartlab.ui.core.models.OutlinedTextFieldUi
import ru.bratusev.smartlab.ui.core.resources.StringsRes
import ru.bratusev.smartlab.ui.core.theme.AppTheme

@Composable
fun InputFieldBlock(
    screenState: LoginState,
    onLoginChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClicked: () -> Unit,
    isButtonEnabled: Boolean = true
) {
    val textFieldModifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)

    Column {
        OutlinedTextFieldComponent(
            modifier = textFieldModifier,
            outlinedTextFieldUi = OutlinedTextFieldUi(
                value = screenState.login,
                placeholder = StringsRes.LOGIN
            ) {
                onLoginChanged(it)
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextFieldComponent(
            modifier = textFieldModifier,
            outlinedTextFieldUi = OutlinedTextFieldUi(
                value = screenState.password,
                placeholder = StringsRes.PASSWORD,
                isSecret = true
            ) {
                onPasswordChanged(it)
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        CustomButton(
            modifier = textFieldModifier,
            customButtonUi = CustomButtonUi(
                title = StringsRes.AUTH,
                textStyle = MaterialTheme.typography.labelLarge,
                isEnabled = isButtonEnabled
            )
        ) {
            onLoginClicked()
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun InputFieldBlockPreview() {
    AppTheme {
        InputFieldBlock(
            screenState = LoginState(),
            onLoginChanged = {},
            onPasswordChanged = { },
            onLoginClicked = {}
        )
    }
}