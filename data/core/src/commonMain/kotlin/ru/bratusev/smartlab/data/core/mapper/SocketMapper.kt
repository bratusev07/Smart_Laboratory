package ru.bratusev.smartlab.data.core.mapper

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import ru.bratusev.smartlab.data.core.model.ServiceEntity
import ru.bratusev.smartlab.data.core.model.ServiceEntityAttributes
import ru.bratusev.smartlab.domain.core.model.socket.ServiceEntity as DomainServiceEntity
import ru.bratusev.smartlab.domain.core.model.socket.ServiceEntityAttributes as DomainServiceEntityAttributes

fun mapJsonToServiceEntityList(jsonString: String): List<ServiceEntity> {
    val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }
    val root = json.parseToJsonElement(jsonString).jsonObject
    val event = root["event"]?.jsonObject ?: error("Field 'event' not found in JSON")
    val entities = event["a"]?.jsonObject ?: error("Field 'event.a' not found in JSON")
    return entities.map { (key, value) ->
        val entity = json.decodeFromJsonElement<ServiceEntity>(value)
        val attributes = json.decodeFromJsonElement<ServiceEntityAttributes>(entity.rawAttributes!!)
        entity.copy(id = key, domain = key.split(".")[0], attributes = attributes)
    }
}


fun mapJsonToEventPair(jsonString: String): Pair<String, String> {
    val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }

    val root = json.parseToJsonElement(jsonString).jsonObject
    val event = root["event"]?.jsonObject ?: error("Field 'event' not found in JSON")
    val entity = event["c"]?.jsonObject ?: error("Field 'event.c' not found in JSON")

    val entityId = entity.keys.first()
    val entityState = entity[entityId]?.jsonObject["+"]?.jsonObject["s"].toString()
    return entityId to entityState
}

fun ServiceEntityAttributes.toDomain() = DomainServiceEntityAttributes(
    icon = icon,
    friendlyName = friendlyName,
    deviceClass = deviceClass,
)

internal fun ServiceEntity.mapToDomain() = DomainServiceEntity(
    state = state.toString(),
    attributes = attributes?.toDomain(),
    c = c.toString(),
    id = id,
    domain = domain,
    lastUpdate = lastUpdate,
    lastChange = lastChange,
)