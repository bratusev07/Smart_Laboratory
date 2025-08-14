package ru.bratusev.smartlab.data.core

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import ru.bratusev.smartlab.data.core.model.ServiceEntity

fun Int.getAndIncrement() : Int {
    val id = this
    this.plus(1)
    return id
}

fun parseToServiceEntityMap(jsonString: String): List<ServiceEntity> {
    val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }
    val root = json.parseToJsonElement(jsonString).jsonObject
    val event = root["event"]?.jsonObject ?: error("Field 'event' not found in JSON")
    val entities = event["a"]?.jsonObject ?: error("Field 'event.a' not found in JSON")
    return entities.map { (key, value) ->
        val entity = json.decodeFromJsonElement<ServiceEntity>(value)
        entity.copy(id = key, domain = key.split(".")[0])
    }
}

/**
 * Получает текущую дату и время в виде пары (дата, время)
 */
fun getCurrentDateTime(): Pair<String, String> {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val time = "${now.hour.toString().padStart(2, '0')}:${now.minute.toString().padStart(2, '0')}:${now.second.toString().padStart(2, '0')}"
    val date = "${now.dayOfMonth.toString().padStart(2, '0')}.${now.monthNumber.toString().padStart(2, '0')}.${now.year}"
    return date to time
}