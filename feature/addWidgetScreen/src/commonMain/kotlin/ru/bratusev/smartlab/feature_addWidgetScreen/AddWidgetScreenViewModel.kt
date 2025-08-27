package ru.bratusev.smartlab.feature_addWidgetScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.bratusev.smartlab.domain.core.model.CustomWidget
import ru.bratusev.smartlab.domain.core.usecase.GetCustomWidgetsUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetLoggerUseCase
import ru.bratusev.smartlab.domain.core.usecase.SetCustomWidgetsUseCase
import ru.bratusev.smartlab.feature_addWidgetScreen.models.AddWidgetScreenState
import ru.bratusev.smartlab.feature_addWidgetScreen.models.Event
import kotlin.reflect.KClass

class AddWidgetScreenViewModel(
    private val logger: GetLoggerUseCase,
    private val getWidgetsUseCase: GetCustomWidgetsUseCase,
    private val saveWidgetUseCase: SetCustomWidgetsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddWidgetScreenState())
    val uiState: StateFlow<AddWidgetScreenState> = _uiState

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun saveWidget(widget: KClass<out CustomWidget>) {
        getWidgetsUseCase()
            .map { result -> result.getOrThrow() }
            .flatMapLatest { currentWidgets ->
                val mutableWidgets = currentWidgets.toMutableList()
                val newId = (mutableWidgets.maxOfOrNull { it.id } ?: 0) + 1

                val actualWidget = when (widget) {
                    CustomWidget.SensorsList::class -> {
                        CustomWidget.SensorsList(sensorsIds = emptyList(), id = newId)
                    }

                    else -> {
                        throw IllegalArgumentException("Unsupported widget type: $widget")
                    }
                }

                mutableWidgets.add(actualWidget)

                saveWidgetUseCase(mutableWidgets)
            }
            .onEach { saveResult ->
                saveResult.fold(
                    onSuccess = {
                        logger.d(
                            "Save widgets useCase",
                            "Successfully saved new widget."
                        )
                    },
                    onFailure = { error -> logger.e("Save widgets useCase", error.toString()) }
                )
            }
            .catch { error ->
                logger.e("SaveWidget Chain", "An error occurred: ${error.message}")
            }
            .launchIn(viewModelScope)
    }

    internal fun handleEvent(event: Event) {
        when (event) {
            is Event.OnSaveWidget -> saveWidget(event.widget)
        }
    }
}
