package ru.bratusev.smartlab.feature_login.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HideSource
import androidx.compose.material.icons.filled.KeyboardHide
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.feature_login.models.InternalLoginState
import ru.bratusev.smartlab.feature_login.models.LoginState
import ru.bratusev.smartlab.ui.core.components.CustomButton
import ru.bratusev.smartlab.ui.core.components.OutlinedTextFieldComponent
import ru.bratusev.smartlab.ui.core.models.CustomButtonUi
import ru.bratusev.smartlab.ui.core.models.OutlinedTextFieldUi
import ru.bratusev.smartlab.ui.core.theme.AppTheme
import smartlaboratory.ui.core.generated.resources.Res
import smartlaboratory.ui.core.generated.resources.auth_button
import smartlaboratory.ui.core.generated.resources.login
import smartlaboratory.ui.core.generated.resources.password

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
                shape = MaterialTheme.shapes.medium,
                value = screenState.login,
                placeholder = stringResource(Res.string.login)
            ) {
                onLoginChanged(it)
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextFieldComponent(
            modifier = textFieldModifier,
            outlinedTextFieldUi = OutlinedTextFieldUi(
                shape = MaterialTheme.shapes.medium,
                value = screenState.password,
                placeholder = stringResource(
                    Res.string.password
                ),
                enableHidingPassword = true
            ) {
                onPasswordChanged(it)
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        CustomButton(
            modifier = textFieldModifier,
            customButtonUi = CustomButtonUi(
                title = stringResource(Res.string.auth_button),
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
            screenState = LoginState(
                internalLoginState = InternalLoginState()
            ),
            onLoginChanged = {},
            onPasswordChanged = { },
            onLoginClicked = {}
        )
    }
}