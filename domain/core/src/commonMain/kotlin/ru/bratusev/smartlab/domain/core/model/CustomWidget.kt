package ru.bratusev.smartlab.domain.core.model

sealed class CustomWidget {
    abstract val id: Int

    class SensorsList(val sensorsIds: List<String>, override val id: Int) :
        CustomWidget()
}