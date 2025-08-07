package ru.bratusev.smartlab.data.core

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import ru.bratusev.smartlab.data.core.model.ServiceEntity
import kotlin.collections.component1
import kotlin.collections.component2

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