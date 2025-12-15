package ru.bratusev.smartlab.ui.core.models

data class AutomationUi(
    val automationList: List<AutomationItemUi>
)

data class AutomationItemUi(
    val id: String = "",
    val alias: String = "",
    val description: String = "",
    val triggers: List<TriggerUi> = emptyList(),
    val actions: List<ActionWrapperUi> = emptyList(),
    val mode: String = "single"
)

data class TriggerUi(val trigger: String, val entityId: List<String>, val to: String?)

data class ActionWrapperUi(val action: String, val target: TargetUi)

data class TargetUi(val entityId: String)