package ru.bratusev.smartlab.data.core

import Automation
import io.ktor.http.ContentType.Application.Yaml
import kotlinx.serialization.builtins.ListSerializer

actual object YamlParser {

    val serializer = Automation.serializer()
    /*private val automationSerializerRef = AtomicReference<KSerializer<Automation>?>(null)

    private val automationSerializer: KSerializer<Automation>
        get() {
            val existing = automationSerializerRef.value
            if (existing != null) return existing

            val serializer = Automation.serializer()
            automationSerializerRef.value = serializer
            return serializer
        }*/

    actual fun parseAutomations(yamlText: String): List<Automation> {
        return emptyList()/*Yaml.default.decodeFromString(
            ListSerializer(serializer),
            yamlText
        )*/
    }

    actual fun parseAutomationsToYaml(automations: List<Automation>): String {
        return ""/*Yaml.default.encodeToString(
            ListSerializer(serializer),
            automations
        ).lines()
            .filter { !it.contains("null") }
            .joinToString("\n")*/
    }
}