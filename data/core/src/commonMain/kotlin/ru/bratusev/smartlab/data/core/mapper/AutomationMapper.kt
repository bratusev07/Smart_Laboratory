package ru.bratusev.smartlab.data.core.mapper

import ActionWrapperDTO
import AutomationDTO
import ChooseBranchDTO
import ConditionDTO
import TargetDTO
import TriggerDTO
import ru.bratusev.smartlab.domain.core.model.automation.ActionWrapper
import ru.bratusev.smartlab.domain.core.model.automation.Automation
import ru.bratusev.smartlab.domain.core.model.automation.ChooseBranch
import ru.bratusev.smartlab.domain.core.model.automation.Condition
import ru.bratusev.smartlab.domain.core.model.automation.Target
import ru.bratusev.smartlab.domain.core.model.automation.Trigger

internal fun AutomationDTO.mapToDomain() = Automation(
    id = id,
    alias = alias,
    description = description,
    triggers = triggers.map { it.mapToDomain() },
    conditions = conditions.map { it.mapToDomain() },
    actions = actions.map { it.mapToDomain() },
    mode = mode
)

internal fun TriggerDTO.mapToDomain() = Trigger(
    trigger = trigger,
    entityId = entityId,
    above = above,
    below = below,
    at = at,
    allowedMethods = allowed_methods,
    localOnly = local_only,
    webhookId = webhook_id
)

internal fun ConditionDTO.mapToDomain() = Condition(
    condition = condition,
    entityId = entityId,
    state = state,
    above = above,
    below = below,
    after = after,
    before = before,
    valueTemplate = valueTemplate
)

internal fun ActionWrapperDTO.mapToDomain(): ActionWrapper = ActionWrapper(
    action = action,
    data = data,
    metadata = metadata,
    target = target?.mapToDomain(),
    responseVariable = response_variable,
    choose = choose?.map { it.mapToDomain() },
    ifBlock = ifBlock?.map { it.mapToDomain() },
    then = then?.map { it.mapToDomain() },
    elseBlock = elseBlock?.map { it.mapToDomain() },
)

internal fun TargetDTO.mapToDomain() = Target(
    entityId = entityId
)

internal fun ChooseBranchDTO.mapToDomain() = ChooseBranch(
    conditions = conditions.map { it.mapToDomain() },
    sequence = sequence.map { it.mapToDomain() }
)