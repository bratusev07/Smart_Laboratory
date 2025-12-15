package ru.bratusev.smartlab.ui.core.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.bratusev.smartlab.ui.core.models.ActionWrapperUi
import ru.bratusev.smartlab.ui.core.models.AutomationItemUi
import ru.bratusev.smartlab.ui.core.models.SensorEntityUi
import ru.bratusev.smartlab.ui.core.models.TargetUi
import ru.bratusev.smartlab.ui.core.models.TriggerUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutomationSetupBottomSheet(
    entities: List<SensorEntityUi>,
    onDismiss: () -> Unit,
    onCreateAutomation: (AutomationItemUi) -> Unit
) {
    val vm = remember { AutomationSheetViewModel() }

    val triggerCandidates = remember { entities }
    val actionCandidates = remember { entities.filter { it.domain in listOf("light", "switch", "climate", "cover") } }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            SectionTitle("Если…")

            EntityDropdown(
                entities = triggerCandidates,
                selected = vm.selectedTriggerEntity,
                label = "Сенсор-триггер",
                onSelect = { vm.selectedTriggerEntity = it }
            )

            if (vm.selectedTriggerEntity != null) {
                Spacer(Modifier.height(8.dp))
                Text("Состояние сенсора")

                val states = listOf("on", "off", "open", "closed", "detected", "idle", "true", "false")

                var expanded by remember { mutableStateOf(false) }

                Box {
                    OutlinedButton(onClick = { expanded = true }) {
                        Text(vm.selectedTriggerState ?: "Выбрать…")
                    }

                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        states.forEach { st ->
                            DropdownMenuItem(
                                text = { Text(st) },
                                onClick = {
                                    expanded = false
                                    vm.selectedTriggerState = st
                                }
                            )
                        }
                    }
                }
            }

            SectionTitle("То…")

            EntityDropdown(
                entities = actionCandidates,
                selected = vm.selectedActionEntity,
                label = "Устройство",
                onSelect = {
                    vm.selectedActionEntity = it
                    vm.selectedActionService = null
                }
            )

            if (vm.selectedActionEntity != null) {
                Spacer(Modifier.height(8.dp))
                Text("Действие")

                val services = defaultServicesByDomain[vm.selectedActionEntity!!.domain] ?: emptyList()

                var expanded by remember { mutableStateOf(false) }

                Box {
                    OutlinedButton(onClick = { expanded = true }) {
                        Text(vm.selectedActionService ?: "Выбрать…")
                    }

                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        services.forEach { srv ->
                            DropdownMenuItem(
                                text = { Text(srv) },
                                onClick = {
                                    expanded = false
                                    vm.selectedActionService = srv
                                }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    val automation = vm.buildAutomation(
                        alias = "Auto: ${vm.selectedTriggerEntity?.displayName} → ${vm.selectedActionEntity?.displayName}"
                    )
                    onCreateAutomation(automation)
                    onDismiss()
                },
                enabled = vm.isValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Создать автоматизацию")
            }
        }
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(vertical = 12.dp)
    )
}

@Composable
fun EntityDropdown(
    entities: List<SensorEntityUi>,
    selected: SensorEntityUi?,
    label: String,
    onSelect: (SensorEntityUi) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(text = label)
        Box {
            OutlinedButton(onClick = { expanded = true }) {
                Text(selected?.displayName ?: "Выбрать…")
            }

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                entities.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item.displayName) },
                        onClick = {
                            expanded = false
                            onSelect(item)
                        }
                    )
                }
            }
        }
    }
}


class AutomationSheetViewModel {

    var selectedTriggerEntity by mutableStateOf<SensorEntityUi?>(null)
    var selectedTriggerState by mutableStateOf<String?>(null)

    var selectedActionEntity by mutableStateOf<SensorEntityUi?>(null)
    var selectedActionService by mutableStateOf<String?>(null)

    val isValid get() =
        selectedTriggerEntity != null &&
                selectedTriggerState != null &&
                selectedActionEntity != null &&
                selectedActionService != null

    fun buildAutomation(alias: String): AutomationItemUi {
        val trigger = TriggerUi(
            trigger = "state",
            entityId = listOf(selectedTriggerEntity!!.entityId),
            to = selectedTriggerState
        )

        val action = ActionWrapperUi(
            action = selectedActionService!!,
            target = TargetUi(entityId = selectedActionEntity!!.entityId)
        )

        return AutomationItemUi(
            id = "random id",
            alias = alias,
            triggers = listOf(trigger),
            actions = listOf(action),
            mode = "single"
        )
    }
}

private val defaultServicesByDomain = mapOf(
    "light" to listOf("light.turn_on", "light.turn_off", "light.toggle"),
    "switch" to listOf("switch.turn_on", "switch.turn_off", "switch.toggle"),
    "climate" to listOf("climate.set_temperature", "climate.set_hvac_mode"),
    "cover" to listOf("cover.open_cover", "cover.close_cover")
)
