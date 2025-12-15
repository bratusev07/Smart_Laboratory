import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AutomationDTO(
    val id: String,
    val alias: String,
    val description: String = "",
    val triggers: List<TriggerDTO>,
    val conditions: List<ConditionDTO> = emptyList(),
    val actions: List<ActionWrapperDTO>,
    val mode: String
)

@Serializable
data class TriggerDTO(
    val trigger: String,
    @SerialName("entity_id") val entityId: List<String>? = null,
    val above: Double? = null,
    val below: Double? = null,
    val at: String? = null,
    val allowed_methods: List<String>? = null,
    val local_only: Boolean? = null,
    val webhook_id: String? = null
)

@Serializable
data class ConditionDTO(
    val condition: String,
    @SerialName("entity_id") val entityId: String? = null,
    val state: String? = null,
    val above: Double? = null,
    val below: Double? = null,
    val after: String? = null,
    val before: String? = null,
    @SerialName("value_template") val valueTemplate: String? = null
)

@Serializable
data class ActionWrapperDTO(
    val action: String? = null,
    val data: Map<String, String?>? = null,
    val metadata: Map<String, String?>? = null,
    val target: TargetDTO? = null,
    val response_variable: String? = null,
    val choose: List<ChooseBranchDTO>? = null,
    @SerialName("if") val ifBlock: List<ConditionDTO>? = null,
    val then: List<ActionWrapperDTO>? = null,
    @SerialName("else") val elseBlock: List<ActionWrapperDTO>? = null,
)

@Serializable
data class ChooseBranchDTO(
    val conditions: List<ConditionDTO>,
    val sequence: List<ActionWrapperDTO>
)

@Serializable
data class TargetDTO(
    @SerialName("entity_id") val entityId: String? = null
)