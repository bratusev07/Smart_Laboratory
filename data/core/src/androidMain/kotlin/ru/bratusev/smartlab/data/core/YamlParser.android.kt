package ru.bratusev.smartlab.data.core

import AutomationDTO
import com.charleskorn.kaml.Yaml
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer

actual object YamlParser {

    private var yaml: Yaml = Yaml()

    private val automationDTOSerializer: KSerializer<AutomationDTO> by lazy {
        var serializer: KSerializer<AutomationDTO>?
        var attempts = 0
        do {
            serializer = AutomationDTO.serializer()
            if (serializer == null) {
                attempts++
                if (attempts > 100) error("Failed to obtain Automation.serializer() after retries")
                Thread.sleep(1)
            }
        } while (serializer == null)
        serializer
    }

    actual fun parseAutomations(yamlText: String): List<AutomationDTO> {
        return yaml.decodeFromString(
            ListSerializer(automationDTOSerializer),
            yamlText
        )
    }

    actual fun parseAutomationsToYaml(automationDTOS: List<AutomationDTO>): String {
        return yaml.encodeToString(
            ListSerializer(automationDTOSerializer),
            automationDTOS
        ).lines().filter { !it.contains("null") }.joinToString("\n")
    }
}