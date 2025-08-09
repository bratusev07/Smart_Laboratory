package ru.bratusev.smartlab.ui.core.models.tileButton

import org.jetbrains.compose.resources.DrawableResource
import smartlaboratory.ui.core.generated.resources.Res
import smartlaboratory.ui.core.generated.resources.light_bulb
import smartlaboratory.ui.core.generated.resources.thermometer

sealed class TileButtonUi {
    abstract val isEnabled: Boolean
    abstract val resource: DrawableResource

    class LightBulb(
        val location: String,
        val isOn: Boolean,
        override val isEnabled: Boolean = true,
        override val resource: DrawableResource = TileButtonRes.lightBulb,
    ) : TileButtonUi()

    class Thermometer(
        val location: String,
        val temperature: Float,
        override val isEnabled: Boolean = true,
        override val resource: DrawableResource = TileButtonRes.thermometer,
    ) : TileButtonUi()
}

object TileButtonRes {
    val lightBulb = Res.drawable.light_bulb
    val thermometer = Res.drawable.thermometer
}