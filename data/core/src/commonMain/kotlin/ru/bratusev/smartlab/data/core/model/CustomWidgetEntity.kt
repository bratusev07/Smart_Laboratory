package ru.bratusev.smartlab.data.core.model

import kotlinx.serialization.Serializable
import ru.bratusev.smartlab.domain.core.model.CustomWidget

@Serializable
sealed class CustomWidgetEntity {
    abstract val position: Int
    abstract fun toDomain(): CustomWidget

    @Serializable
    class SensorsList(val sensorsIds: List<String>, override val position: Int) :
        CustomWidgetEntity() {
        override fun toDomain(): CustomWidget.SensorsList {
            return CustomWidget.SensorsList(
                sensorsIds = this.sensorsIds,
                position = this.position
            )
        }
    }
}
