package ru.bratusev.smartlab.data.core

import AutomationDTO

expect object YamlParser {

    fun parseAutomations(yamlText: String): List<AutomationDTO>

    fun parseAutomationsToYaml(automationDTOS: List<AutomationDTO>): String
}