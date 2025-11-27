package ru.bratusev.smartlab.ui.core.models

data class AutomationUi(
    val automationList: List<AutomationItemUi>
)

data class AutomationItemUi(
    val id: String,
    val alias: String,
    val description: String
)
