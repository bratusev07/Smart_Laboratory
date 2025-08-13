package ru.bratusev.smartlab.feature_home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.context.startKoin
import ru.bratusev.smartlab.ui.core.components.CustomButton
import ru.bratusev.smartlab.ui.core.models.CustomButtonUi
import ru.bratusev.smartlab.ui.core.theme.AppTheme

@Composable
fun HomeScreen(
    vm: HomeViewModel = koinViewModel()
) {
    val state = vm.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        CustomButton(
            modifier = Modifier.align(Alignment.Center), customButtonUi = CustomButtonUi(
                state.value.screenName,
                fontWeight = 50,
            )
        ) {
           // t o d o
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    startKoin {
        modules(homeModulePreview)
    }
    AppTheme {
        HomeScreen()
    }
}
