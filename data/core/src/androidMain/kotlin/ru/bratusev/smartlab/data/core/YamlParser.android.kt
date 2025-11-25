package ru.bratusev.smartlab.data.core

import Automation
import com.charleskorn.kaml.Yaml
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer

actual object YamlParser {
    private val automationSerializer: KSerializer<Automation> by lazy {
        var serializer: KSerializer<Automation>?
        var attempts = 0
        do {
            serializer = Automation.serializer()
            if (serializer == null) {
                attempts++
                if (attempts > 100) error("Failed to obtain Automation.serializer() after retries")
                Thread.sleep(1)
            }
        } while (serializer == null)
        serializer
    }

    actual fun parseAutomations(yamlText: String): List<Automation> {
        return Yaml.default.decodeFromString(
            ListSerializer(automationSerializer),
            yamlText
        )
    }

    actual fun parseAutomationsToYaml(automations: List<Automation>): String {
        return Yaml.default.encodeToString(
            ListSerializer(automationSerializer),
            automations
        ).lines().filter { !it.contains("null") }.joinToString("\n")
    }
}