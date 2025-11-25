package ru.bratusev.smartlab.data.core

import Automation

actual object YamlParser {
    actual fun parseAutomations(yamlText: String): List<Automation> {
        return emptyList()
    }

    actual fun parseAutomationsToYaml(automations: List<Automation>): String {
        return "empty"
    }
}