package ru.bratusev.smartlab.domain.core.model

sealed class CustomWidget {
    abstract val id: Int
    abstract val title: String?

    abstract fun copy(id: Int = this.id, title: String? = this.title): CustomWidget
    data class SensorsList(
        val sensorsIds: List<String>,
        override val id: Int,
        override val title: String
    ) :
        CustomWidget() {
        override fun copy(
            id: Int,
            title: String?,
        ): CustomWidget = copy(
            id = id,
            title = title ?: id.toString(),
            sensorsIds = this.sensorsIds,
        )
    }

    data class SingleSensor(
        val sensorId: String,
        override val id: Int,
        override val title: String
    ) :
        CustomWidget() {
        override fun copy(
            id: Int,
            title: String?,
        ): CustomWidget = copy(
            id = id,
            title = title ?: id.toString(),
            sensorId = this.sensorId
        )

        companion object {
            const val NO_SENSOR_ID = "Не назначено"
            const val NO_SENSOR_TITLE = "Не назначено"
        }
    }
}