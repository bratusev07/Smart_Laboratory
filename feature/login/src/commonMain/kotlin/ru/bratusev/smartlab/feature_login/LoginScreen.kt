package ru.bratusev.smartlab.feature_login


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel
import ru.bratusev.smartlab.feature_login.models.Event
import ru.bratusev.smartlab.ui.core.components.CustomButton
import ru.bratusev.smartlab.ui.core.models.CustomButtonUi

@Composable
fun LoginScreen(
    vm: LoginViewModel = koinViewModel(), navigateToHome: () -> Unit,
) {
    val state = vm.uiState.collectAsState()
    val currentStageText: String by remember {
        derivedStateOf {
            when (state.value.loginStage) {
                // TODO: Временно просто текст
                LoginStage.NOTHING_0 -> "Авторизация еще не началась"
                LoginStage.START_1 -> "Авторизация началась"
                LoginStage.SAVING_TOKEN_2 -> "Сохранение токена"
                LoginStage.CHECKING_TOKEN_3 -> "Проверка токена"
                LoginStage.COMPLETED_4 -> {
                    "Авторизация успешно завершена"
                }

                LoginStage.FAILED_DURING_LOGIN -> "Ошибка во время авторизации"
                LoginStage.FAILED_DURING_TOKEN -> "Ошибка во время сохранения токена"
            }
        }
    }
    LaunchedEffect(state.value.loginStage) {
        if (state.value.loginStage == LoginStage.COMPLETED_4) {
            navigateToHome()
        }
    }

    println(vm.device.toString())

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CustomButton(
            customButtonUi = CustomButtonUi(
                title = "Авторизация", fontWeight = 20
            ),
        ) {
            vm.handleEvent(Event.OnCustomButtonClicked)
        }
        CustomButton(
            customButtonUi = CustomButtonUi(
                title = "Проверить токен", fontWeight = 20
            )
        ) {
            vm.handleEvent(Event.OnCheckTokenButtonClicked)
        }
        AnimatedVisibility(
            modifier = Modifier.width(IntrinsicSize.Min),
            visible = state.value.loginStage !in listOf(
                LoginStage.NOTHING_0, LoginStage.FAILED_DURING_TOKEN, LoginStage.FAILED_DURING_LOGIN
            )
        ) {
            Text(maxLines = 1, text = "Текущий этап: $currentStageText")
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
    }
}