package ru.bratusev.smartlab.data.core

import Automation

expect object YamlParser {

    fun parseAutomations(yamlText: String): List<Automation>

    fun parseAutomationsToYaml(automations: List<Automation>): String
}