package ru.bratusev.smartlab.data.core.model

import kotlinx.serialization.Serializable
import ru.bratusev.smartlab.domain.core.model.CustomWidget

@Serializable
sealed class CustomWidgetEntity {
    abstract val id: Int
    abstract val title: String?
    abstract fun toDomain(): CustomWidget

    @Serializable
    class SensorsList(
        val sensorsIds: List<String>,
        override val title: String,
        override val id: Int
    ) :
        CustomWidgetEntity() {
        override fun toDomain(): CustomWidget {
            return CustomWidget.SensorsList(
                sensorsIds = sensorsIds,
                id = id,
                title = title
            )
        }
    }

    @Serializable
    class SingleSensor(val sensorId: String, override val title: String, override val id: Int) :
        CustomWidgetEntity() {
        override fun toDomain(): CustomWidget {
            return CustomWidget.SingleSensor(
                sensorId = sensorId,
                id = id,
                title = this.title
            )
        }
    }
}
