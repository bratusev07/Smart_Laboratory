package ru.bratusev.smartlab.feature_addWidgetScreen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWidgetScreen(
    vm: AddWidgetScreenViewModel = koinViewModel(),
) {
    val state = vm.uiState.collectAsState()

}

