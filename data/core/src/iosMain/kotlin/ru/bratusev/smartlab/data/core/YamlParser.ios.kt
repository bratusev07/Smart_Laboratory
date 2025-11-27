package ru.bratusev.smartlab.data.core

import AutomationDTO

actual object YamlParser {

    val serializer = AutomationDTO.serializer()
    /*private val automationSerializerRef = AtomicReference<KSerializer<Automation>?>(null)

    private val automationSerializer: KSerializer<Automation>
        get() {
            val existing = automationSerializerRef.value
            if (existing != null) return existing

            val serializer = Automation.serializer()
            automationSerializerRef.value = serializer
            return serializer
        }*/

    actual fun parseAutomations(yamlText: String): List<AutomationDTO> {
        return emptyList()/*Yaml.default.decodeFromString(
            ListSerializer(serializer),
            yamlText
        )*/
    }

    actual fun parseAutomationsToYaml(automationDTOS: List<AutomationDTO>): String {
        return ""/*Yaml.default.encodeToString(
            ListSerializer(serializer),
            automations
        ).lines()
            .filter { !it.contains("null") }
            .joinToString("\n")*/
    }
}