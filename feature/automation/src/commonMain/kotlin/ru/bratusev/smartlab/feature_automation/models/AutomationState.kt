package ru.bratusev.smartlab.feature_automation.models

import ru.bratusev.smartlab.ui.core.models.AutomationItemUi
import ru.bratusev.smartlab.ui.core.models.AutomationUi
import ru.bratusev.smartlab.ui.core.models.SensorEntityUi

data class AutomationState(
    val screenName: String = "Automation Screen",
    val automation: AutomationUi = AutomationUi(emptyList()),
    val sensors: List<SensorEntityUi> = emptyList(),
    val isAutomationLoading: Boolean = true,
    val isServiceLoading: Boolean = true,
    val errorMessage: String? = null,
    val isLoading: Boolean = isAutomationLoading && isServiceLoading
)

sealed class Event {

    data class OnDeleteAutomationClicked(val id: String): Event()

    data class OnUpdateAutomationClicked(val automation: AutomationItemUi): Event()

    data object OnSaveAutomation: Event()

    data class AddAutomation(val automation: AutomationItemUi): Event()
}