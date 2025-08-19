package ru.bratusev.smartlab.ui.core.models.sensorCard

import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.DrawableResource
import ru.bratusev.smartlab.ui.core.theme.SensorCardCommonColors
import smartlaboratory.ui.core.generated.resources.Res
import smartlaboratory.ui.core.generated.resources.light_bulb
import smartlaboratory.ui.core.generated.resources.thermometer

sealed class SensorCardUi {

    abstract val id: String
    abstract val state: SensorCardState
    abstract val domain: String
    abstract val drawableResource: DrawableResource

    abstract val tints: SensorCardTints

    sealed class Tile: SensorCardUi() {
        class Small(
            override val id: String,
            override val state: SensorCardState,
            override val domain: String,
            override val drawableResource: DrawableResource,
            override val tints: SensorCardTints,
        ) : Tile()

        data class Medium(
            val title: String,
            override val id: String,
            override val state: SensorCardState,
            override val domain: String,
            override val drawableResource: DrawableResource,
            override val tints: SensorCardTints,
        ) : Tile()

        data class Large(
            val title: String,
            val description: String,
            override val id: String,
            override val state: SensorCardState,
            override val domain: String,
            override val drawableResource: DrawableResource,
            override val tints: SensorCardTints,
        ) : Tile()
    }

    sealed class Widget: SensorCardUi() {
        data class Row(
            val title: String,
            override val id: String,
            override val state: SensorCardState,
            override val domain: String,
            override val drawableResource: DrawableResource,
            override val tints: SensorCardTints,
        ) : Widget()
    }
}

open class SensorCardTints(
    val on: Color,
    val off: Color,
    val unavailable: Color,
) {
    object Common {
        val LightBulb = SensorCardTints(
            on = SensorCardCommonColors.LightBulb.On,
            off = SensorCardCommonColors.LightBulb.Off,
            unavailable = SensorCardCommonColors.LightBulb.Unavailable,
        )

        val Thermometer = SensorCardTints(
            on = SensorCardCommonColors.Thermometer.hot,
            off = SensorCardCommonColors.Thermometer.cold,
            unavailable = SensorCardCommonColors.Thermometer.Unavailable,
        )
    }

    class SingleColor(color: Color) : SensorCardTints(
        on = color, off = color, unavailable = color
    )

    class WithoutOff(on: Color, unavailable: Color) : SensorCardTints(
        on = on, off = unavailable, unavailable = unavailable
    )
}

enum class SensorCardState(stateName: String) {
    On("on"), Off("off"), Unavailable("unavailable"),
}

object SensorCardRes {
    val lightBulb = Res.drawable.light_bulb
    val thermometer = Res.drawable.thermometer
}