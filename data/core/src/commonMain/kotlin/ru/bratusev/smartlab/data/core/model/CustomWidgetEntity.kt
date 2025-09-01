package ru.bratusev.smartlab.data.core.model

import kotlinx.serialization.Serializable
import ru.bratusev.smartlab.domain.core.model.CustomWidget

@Serializable
sealed class CustomWidgetEntity {
    abstract val id: Int
    abstract fun toDomain(): CustomWidget

    @Serializable
    class SensorsList(val sensorsIds: List<String>, override val id: Int) :
        CustomWidgetEntity() {
        override fun toDomain(): CustomWidget {
            return CustomWidget.SensorsList(
                sensorsIds = this.sensorsIds,
                id = this.id
            )
        }
    }

    @Serializable
    class SingleSensor(val sensorId: String, override val id: Int) :
        CustomWidgetEntity() {
        override fun toDomain(): CustomWidget {
            return CustomWidget.SingleSensor(
                sensorId = sensorId,
                id = id
            )
        }
    }
}
