package ru.bratusev.smartlab.data.core.message

import kotlinx.serialization.json.JsonElement
import ru.bratusev.smartlab.data.core.model.AreaEntity
import ru.bratusev.smartlab.data.core.model.ServiceEntity

interface HomeAssistantMessageHandlers {
    fun handleAuthRequired(jsonElement: JsonElement, accessToken: String?)

    fun handleAuthOk(jsonElement: JsonElement, getMessageId: () -> Int, setMessageId: (Int) -> Unit)

    fun handleAuthInvalid(jsonElement: JsonElement)

    fun handleResult(
        jsonElement: JsonElement,
        emitAreaEntity: (List<AreaEntity>) -> Boolean,
        emitAreaDevices: (List<String>) -> Unit
    )

    fun handleEvent(
        jsonElement: JsonElement,
        getServiceEntitiesCopy: () -> List<ServiceEntity>,
        setServiceEntitiesCopy: (List<ServiceEntity>) -> Unit,
        emitServiceEntities: (List<ServiceEntity>) -> Boolean,
    )

    fun handlePong(jsonElement: JsonElement)
}