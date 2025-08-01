package ru.bratusev.smartlab.ui.core.models

sealed class TileButtonUi {
    abstract val isEnabled: Boolean

    class LightBulb(
        val location: String, val isOn: Boolean, override val isEnabled: Boolean = true
    ) : TileButtonUi()

    class Thermometer(
        val location: String, val temperature: Float, override val isEnabled: Boolean = true
    ) : TileButtonUi()
}