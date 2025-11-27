package ru.bratusev.smartlab.data.core.repository

import AutomationDTO
import io.ktor.client.HttpClient
import io.ktor.client.request.cookie
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.formUrlEncode
import ru.bratusev.smartlab.data.core.YamlParser
import ru.bratusev.smartlab.data.core.mapper.mapToDomain
import ru.bratusev.smartlab.data.core.remote_storage.Constants.BASE_URL
import ru.bratusev.smartlab.data.core.remote_storage.Constants.CONFIG_FILE_PATH
import ru.bratusev.smartlab.domain.core.model.automation.Automation
import ru.bratusev.smartlab.domain.core.repository.AutomationRepository
import ru.bratusev.smartlab.domain.core.repository.SocketRepository

class AutomationRepositoryImpl(
    val client: HttpClient,
    val socketRepository: SocketRepository
) : AutomationRepository {

    override suspend fun saveAutomation(automationData: String) {
        val requestBody = Parameters.build {
            append("filename", CONFIG_FILE_PATH)
            append("text", automationData)
        }.formUrlEncode()

        val saveFileUrl = "$BASE_URL$shortFileUrl/api/save"
        client.post(saveFileUrl) {
            cookie("ingress_session", sessionId)
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(requestBody)
        }.bodyAsText()
    }

    override suspend fun fetchAutomaton(url: String): List<Automation> {
        val yaml = "- id: '1725803458321'\n" +
                "  alias: Включение/выключение панели от кол-ва сотрудников в офисе\n" +
                "  description: ''\n" +
                "  triggers:\n" +
                "  - entity_id:\n" +
                "    - sensor.skb_employees\n" +
                "    above: 0\n" +
                "    trigger: numeric_state\n" +
                "  - entity_id:\n" +
                "    - sensor.skb_employees\n" +
                "    below: 1\n" +
                "    trigger: numeric_state\n" +
                "  - at: '19:00:00'\n" +
                "    trigger: time\n" +
                "  conditions: []\n" +
                "  actions:\n" +
                "  - choose:\n" +
                "    - conditions:\n" +
                "      - condition: time\n" +
                "        after: '19:00:00'\n" +
                "      sequence:\n" +
                "      - action: switch.turn_off\n" +
                "        metadata: {}\n" +
                "        data: {}\n" +
                "        target:\n" +
                "          entity_id: switch.skb_panel\n" +
                "      - action: switch.turn_off\n" +
                "        metadata: {}\n" +
                "        data: {}\n" +
                "        target:\n" +
                "          entity_id: switch.museum\n" +
                "    - conditions:\n" +
                "      - condition: numeric_state\n" +
                "        entity_id: sensor.skb_employees\n" +
                "        above: 0\n" +
                "      sequence:\n" +
                "      - action: switch.turn_on\n" +
                "        metadata: {}\n" +
                "        data: {}\n" +
                "        target:\n" +
                "          entity_id: switch.skb_panel\n" +
                "      - action: switch.turn_on\n" +
                "        metadata: {}\n" +
                "        data: {}\n" +
                "        target:\n" +
                "          entity_id: switch.museum\n" +
                "    - conditions:\n" +
                "      - condition: numeric_state\n" +
                "        entity_id: sensor.skb_employees\n" +
                "        below: 1\n" +
                "      sequence:\n" +
                "      - action: switch.turn_off\n" +
                "        metadata: {}\n" +
                "        data: {}\n" +
                "        target:\n" +
                "          entity_id: switch.skb_panel\n" +
                "      - action: switch.turn_off\n" +
                "        metadata: {}\n" +
                "        data: {}\n" +
                "        target:\n" +
                "          entity_id: switch.museum\n" +
                "  mode: single\n" +
                "- id: '1736522008964'\n" +
                "  alias: Test webhook\n" +
                "  description: ''\n" +
                "  triggers:\n" +
                "  - trigger: webhook\n" +
                "    allowed_methods:\n" +
                "    - POST\n" +
                "    - PUT\n" +
                "    local_only: true\n" +
                "    webhook_id: -eRw_AZR5-2j40lwMs8SqehYn\n" +
                "  conditions: []\n" +
                "  actions: []\n" +
                "  mode: single\n" +
                "- id: '1739734840159'\n" +
                "  alias: Отправка текста на панель\n" +
                "  description: ''\n" +
                "  triggers:\n" +
                "  - trigger: state\n" +
                "    entity_id:\n" +
                "    - input_text.ledpanel_text\n" +
                "  conditions: []\n" +
                "  actions:\n" +
                "  - action: rest_command.ledpanel_message\n" +
                "    metadata: {}\n" +
                "    data:\n" +
                "      message: '{{ states(''input_text.ledpanel_text'') }}'\n" +
                "    response_variable: ledpanel_response\n" +
                "  - if:\n" +
                "    - condition: template\n" +
                "      value_template: '{{ ledpanel_response[''status''] == 200 }}'\n" +
                "    then:\n" +
                "    - action: input_number.set_value\n" +
                "      target:\n" +
                "        entity_id: input_number.test\n" +
                "      data:\n" +
                "        value: 1\n" +
                "    else:\n" +
                "    - action: input_number.set_value\n" +
                "      target:\n" +
                "        entity_id: input_number.test\n" +
                "      data:\n" +
                "        value: 2\n" +
                "  mode: single\n" +
                "- id: '1742047445853'\n" +
                "  alias: Led-панель переключение режимов\n" +
                "  description: ''\n" +
                "  triggers:\n" +
                "  - trigger: state\n" +
                "    entity_id:\n" +
                "    - input_select.ledpanel_state\n" +
                "  conditions: []\n" +
                "  actions:\n" +
                "  - choose:\n" +
                "    - conditions:\n" +
                "      - condition: state\n" +
                "        entity_id: input_select.ledpanel_state\n" +
                "        state: Надпись СКБ\n" +
                "      sequence:\n" +
                "      - action: rest_command.skb_logo\n" +
                "        data: {}\n" +
                "    - conditions:\n" +
                "      - condition: state\n" +
                "        entity_id: input_select.ledpanel_state\n" +
                "        state: Бегущая строка\n" +
                "      sequence:\n" +
                "      - action: rest_command.scroll_text\n" +
                "        data: {}\n" +
                "  mode: single\n"
        // TODO Remove hardcode yaml
        return try {
            YamlParser.parseAutomations(yaml).map { it.mapToDomain() }
        } catch (e : Exception) {
            emptyList()
        }

        /*shortFileUrl = url.replace("\"", "")
        val fileUrl = "$BASE_URL$shortFileUrl/api/file?filename=$CONFIG_FILE_PATH"
        socketRepository.fetchIngressSessionId().let { id ->
            sessionId = id.replace("\"", "")
            client.get(fileUrl) {
                contentType(ContentType.Application.Yaml)
                cookie("ingress_session", sessionId)
            }.bodyAsText().let { yaml ->
                return try {
                    YamlParser.parseAutomations(yaml).map { it.mapToDomain() }
                } catch (_ : Exception) {
                    emptyList()
                }
            }
        }*/
    }

    companion object {
        var sessionId: String = ""
        var shortFileUrl: String = ""
    }
}