package ru.bratusev.smartlab.domain.core.model.automation

data class Automation(
    val id: String,
    val alias: String,
    val description: String = "",
    val triggers: List<Trigger>,
    val conditions: List<Condition> = emptyList(),
    val actions: List<ActionWrapper>,
    val mode: String
)

data class Trigger(
    val trigger: String,
    val entityId: List<String>? = null,
    val above: Double? = null,
    val below: Double? = null,
    val at: String? = null,
    val allowedMethods: List<String>? = null,
    val localOnly: Boolean? = null,
    val webhookId: String? = null
)

data class Condition(
    val condition: String,
    val entityId: String? = null,
    val state: String? = null,
    val above: Double? = null,
    val below: Double? = null,
    val after: String? = null,
    val before: String? = null,
    val valueTemplate: String? = null
)

data class ActionWrapper(
    val action: String? = null,
    val data: Map<String, String?>? = null,
    val metadata: Map<String, String?>? = null,
    val target: Target? = null,
    val responseVariable: String? = null,
    val choose: List<ChooseBranch>? = null,
    val ifBlock: List<Condition>? = null,
    val then: List<ActionWrapper>? = null,
    val elseBlock: List<ActionWrapper>? = null,
)

data class ChooseBranch(
    val conditions: List<Condition>,
    val sequence: List<ActionWrapper>
)

data class Target(
    val entityId: String? = null
)