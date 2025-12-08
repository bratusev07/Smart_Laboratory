package ru.bratusev.smartlab.feature_automation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.bratusev.smartlab.domain.core.model.automation.ActionWrapper
import ru.bratusev.smartlab.domain.core.model.automation.Automation
import ru.bratusev.smartlab.domain.core.model.automation.Target
import ru.bratusev.smartlab.domain.core.model.automation.Trigger
import ru.bratusev.smartlab.domain.core.usecase.GetAutomationUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetLoggerUseCase
import ru.bratusev.smartlab.domain.core.usecase.GetServiceEntitiesUseCase
import ru.bratusev.smartlab.domain.core.usecase.UpdateAutomationUseCase
import ru.bratusev.smartlab.feature_automation.mappers.mapToUi
import ru.bratusev.smartlab.feature_automation.models.AutomationState
import ru.bratusev.smartlab.feature_automation.models.Event
import ru.bratusev.smartlab.ui.core.models.AutomationItemUi
import ru.bratusev.smartlab.ui.core.models.AutomationUi

class AutomationViewModel(
    private val loggerUseCase: GetLoggerUseCase,
    getAutomationUseCase: GetAutomationUseCase,
    private val updateAutomationUseCase: UpdateAutomationUseCase,
    serviceEntitiesUseCase: GetServiceEntitiesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AutomationState())
    val uiState: StateFlow<AutomationState> = _uiState

    internal var originAutomations = emptyList<Automation>()

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
                            ),
                        )
                    )
                },
                onFailure = { e ->
                    e.message.toString()
                }
            )
        }.launchIn(viewModelScope)

        serviceEntitiesUseCase.invoke().onEach { entities ->
            updateState(_uiState.value.copy(sensors = entities.map { it.mapToUi() }))
        }.launchIn(viewModelScope)
    }

    private fun updateState(updatedState: AutomationState) {
        viewModelScope.launch {
            _uiState.emit(updatedState)
        }
    }

    private fun deleteAutomation(id: String) {
        val automations = uiState.value.automation.automationList.filter { it.id != id }
        updateState(uiState.value.copy(automation = AutomationUi(automationList = automations),))
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

        updateState(uiState.value.copy(automation = AutomationUi(automationList = automations),))
    }

    private fun addAutomation(automation: AutomationItemUi) {
        originAutomations = originAutomations.plus(
            Automation(
                id = automation.id,
                alias = automation.alias,
                triggers = automation.triggers.map { trigger ->
                    Trigger(trigger = trigger.trigger, entityId = trigger.entityId)
                },
                actions = automation.actions.map {
                    val action = automation.actions.first()
                    ActionWrapper(
                        action = action.action,
                        target = Target(action.target.entityId)
                    )
                },
                mode = automation.mode
            )
        )
        val updatedAutomations = uiState.value.automation.automationList.plus(automation)
        updateState(uiState.value.copy(automation = AutomationUi(updatedAutomations)))
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
            is Event.AddAutomation -> addAutomation(event.automation)
        }
    }
}