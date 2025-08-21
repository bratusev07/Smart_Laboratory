package ru.bratusev.smartlab.domain.core.model

sealed class CustomWidget {
    abstract val position: Int

    class SensorsList(val sensorsIds: List<String>, override val position: Int) :
        CustomWidget()
}