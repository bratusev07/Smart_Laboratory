package ru.bratusev.smartlab.feature_automation.models

import ru.bratusev.smartlab.ui.core.models.AutomationItemUi
import ru.bratusev.smartlab.ui.core.models.AutomationUi

data class AutomationState(
    val screenName: String = "Automation Screen",
    val automation: AutomationUi = AutomationUi(emptyList())
)

sealed class Event {

    data class OnDeleteAutomationClicked(val id: String): Event()

    data class OnUpdateAutomationClicked(val automation: AutomationItemUi): Event()
}