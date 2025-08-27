package ru.bratusev.smartlab.feature_addWidgetScreen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.bratusev.smartlab.feature_addWidgetScreen.models.AddWidgetScreenState

class AddWidgetScreenViewModel() : ViewModel() {
    private val _uiState = MutableStateFlow(AddWidgetScreenState())
    val uiState: StateFlow<AddWidgetScreenState> = _uiState


}