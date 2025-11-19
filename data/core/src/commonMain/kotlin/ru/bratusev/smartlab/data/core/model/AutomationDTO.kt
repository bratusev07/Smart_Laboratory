import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Automation(
    val id: String,
    val alias: String,
    val description: String = "",
    val triggers: List<Trigger>,
    val conditions: List<Condition> = emptyList(),
    val actions: List<ActionWrapper>,
    val mode: String
)

@Serializable
data class Trigger(
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
data class Condition(
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
data class ActionWrapper(
    val action: String? = null,
    val data: Map<String, String?>? = null,
    val metadata: Map<String, String?>? = null,
    val target: Target? = null,
    val response_variable: String? = null,
    val choose: List<ChooseBranch>? = null,
    @SerialName("if") val ifBlock: List<Condition>? = null,
    val then: List<ActionWrapper>? = null,
    @SerialName("else") val elseBlock: List<ActionWrapper>? = null,
)

@Serializable
data class ChooseBranch(
    val conditions: List<Condition>,
    val sequence: List<ActionWrapper>
)

@Serializable
data class Target(
    @SerialName("entity_id") val entityId: String? = null
)