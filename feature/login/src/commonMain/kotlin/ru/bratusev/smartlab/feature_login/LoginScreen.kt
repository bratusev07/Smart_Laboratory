package ru.bratusev.smartlab.feature_login


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import ru.bratusev.smartlab.feature_login.components.InputFieldBlock
import ru.bratusev.smartlab.feature_login.models.Event
import ru.bratusev.smartlab.feature_login.models.LoginStage
import ru.bratusev.smartlab.ui.core.components.AnimatedLoadComponent
import ru.bratusev.smartlab.ui.core.resources.StringsRes

@Composable
fun LoginScreen(
    vm: LoginViewModel = koinViewModel(), navigateToHome: () -> Unit,
) {
    val state = vm.uiState.collectAsState()

    LaunchedEffect(state.value.loginStage) {
        if (state.value.loginStage == LoginStage.COMPLETED_4) {
            navigateToHome()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = StringsRes.AUTH,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        InputFieldBlock(
            state.value,
            onLoginChanged = { login ->
                vm.handleEvent(Event.OnLoginChanged(login))
            },
            onPasswordChanged = { password ->
                vm.handleEvent(Event.OnPasswordChanged(password))
            },
            onLoginClicked = {
                vm.handleEvent(Event.OnLoginClicked)
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        AnimatedLoadComponent(
            Modifier
                .width(IntrinsicSize.Min)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            state.value.animatedLoadUi
        )
    }
}