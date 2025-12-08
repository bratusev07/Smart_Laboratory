package ru.bratusev.smartlab.feature_automation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.bratusev.smartlab.data.core.Logger
import ru.bratusev.smartlab.domain.core.model.automation.Automation
import ru.bratusev.smartlab.domain.core.usecase.GetAutomationUseCase
import ru.bratusev.smartlab.domain.core.usecase.UpdateAutomationUseCase
import ru.bratusev.smartlab.feature_automation.models.AutomationState
import ru.bratusev.smartlab.feature_automation.models.Event
import ru.bratusev.smartlab.ui.core.models.AutomationItemUi
import ru.bratusev.smartlab.ui.core.models.AutomationUi

class AutomationViewModel(
    private val logger: Logger,
    getAutomationUseCase: GetAutomationUseCase,
    private val updateAutomationUseCase: UpdateAutomationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AutomationState())
    val uiState: StateFlow<AutomationState> = _uiState

    private var originAutomations = emptyList<Automation>()

    init {
        getAutomationUseCase().onEach { result ->
            result.fold(
                onSuccess = {
                    originAutomations = it
                    updateState(
                        _uiState.value.copy(
                            automation = AutomationUi(
                                it.map { automation ->
                                    AutomationItemUi(
                                        automation.id,
                                        automation.alias,
                                        automation.description
                                    )
                                }
                            )
                        )
                    )
                },
                onFailure = { e ->
                    e.message.toString()
                }
            )
        }.launchIn(viewModelScope)
    }

    private fun updateState(updatedState: AutomationState) {
        viewModelScope.launch {
            _uiState.emit(updatedState)
        }
    }

    private fun deleteAutomation(id: String) {
        val automations = uiState.value.automation.automationList.filter { it.id != id }
        updateState(uiState.value.copy(automation = AutomationUi(automationList = automations)))
    }

    private fun updateAutomation(automation: AutomationItemUi) {
        val automations: List<AutomationItemUi> = uiState.value.automation.automationList.map {
            if (it.id == automation.id) {
                it.copy(
                    id = automation.id,
                    alias = automation.alias,
                    description = automation.description
                )
            } else it
        }

        updateState(uiState.value.copy(automation = AutomationUi(automationList = automations)))
    }

    private fun saveAutomations() {
        val uiAutomations = uiState.value.automation.automationList
        val filteredAutomations: List<Automation> = buildList {
            for (id in uiAutomations.map { it.id }) {
                val automation = originAutomations.find { it.id == id }
                if (automation != null) add(automation)
            }
        }

        val updatedAutomations: List<Automation> = filteredAutomations.map { uiAutomation ->
            val automation = uiAutomations.find { it.id == uiAutomation.id } ?: return
            uiAutomation.copy(
                description = automation.description,
                alias = automation.alias
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            updateAutomationUseCase.invoke(updatedAutomations)
        }
    }

    internal fun handleEvent(event: Event) {
        when (event) {
            is Event.OnDeleteAutomationClicked -> deleteAutomation(event.id)
            is Event.OnUpdateAutomationClicked -> updateAutomation(event.automation)
            is Event.OnSaveAutomation -> saveAutomations()
        }
    }
}