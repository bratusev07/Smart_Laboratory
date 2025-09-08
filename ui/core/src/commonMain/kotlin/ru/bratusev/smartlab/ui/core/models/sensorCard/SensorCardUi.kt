package ru.bratusev.smartlab.ui.core.models.sensorCard

import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.DrawableResource
import ru.bratusev.smartlab.ui.core.resources.StringsRes
import ru.bratusev.smartlab.ui.core.theme.SensorCardCommonColors
import smartlaboratory.ui.core.generated.resources.Res
import smartlaboratory.ui.core.generated.resources.light_bulb
import smartlaboratory.ui.core.generated.resources.thermometer

sealed class SensorCardUi {

    abstract val id: String
    abstract val state: SensorState
    abstract val domain: SensorDomain
    abstract val drawableResource: DrawableResource

    abstract val tints: SensorCardTints

    data class Row(
        val title: String,
        override val id: String,
        override val state: SensorState,
        override val domain: SensorDomain,
        override val drawableResource: DrawableResource,
        override val tints: SensorCardTints,
    ) : SensorCardUi()

    sealed class Tile : SensorCardUi() {
        class Small(
            override val id: String,
            override val state: SensorState,
            override val domain: SensorDomain,
            override val drawableResource: DrawableResource,
            override val tints: SensorCardTints,
        ) : Tile()

        data class Medium(
            val title: String,
            override val id: String,
            override val state: SensorState,
            override val domain: SensorDomain,
            override val drawableResource: DrawableResource,
            override val tints: SensorCardTints,
        ) : Tile()

        data class Large(
            val title: String,
            val description: String,
            override val id: String,
            override val state: SensorState,
            override val domain: SensorDomain,
            override val drawableResource: DrawableResource,
            override val tints: SensorCardTints,
        ) : Tile()

        data class Sensor(
            val title: String,
            val measurementUnit: String,
            override val id: String,
            override val state: SensorState.SensorValue,
            override val domain: SensorDomain,
            override val drawableResource: DrawableResource,
            override val tints: SensorCardTints,
        ) : Tile()
    }

    sealed class Widget : SensorCardUi() {
        data class Switch(
            val title: String,
            override val id: String,
            override val state: SensorState,
            override val domain: SensorDomain,
            override val drawableResource: DrawableResource,
            override val tints: SensorCardTints,
        ) : Widget()
    }

    data class Modal(
        val title: String?,
        override val id: String,
        override val state: SensorState,
        override val domain: SensorDomain,
        override val drawableResource: DrawableResource,
        override val tints: SensorCardTints = SensorCardTints.SingleColor(Color.Gray),
    ) : SensorCardUi()
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

}

sealed class SensorState(val localeName: String) {
    data object On : SensorState("Включено")
    data object Off : SensorState("Выключено")
    data object Unavailable : SensorState("Отключено")
    data class SensorValue(val value: Float) : SensorState("Значение сенсора") {
        companion object {
            fun floatFromString(str: String?): SensorValue {
                val string = str?.replace("\"", "")
                val result = string?.toFloatOrNull()
                return SensorValue(result ?: -1f)
            }
        }
    }

    companion object {
        fun fromString(str: String?): SensorState =
            when (str?.lowercase()) {
                "on", "\"on\"" -> On
                "off", "\"off\"" -> Off
                else -> Unavailable
            }
    }

}

enum class SensorDomain(val localeName: String) {
    SWITCH(StringsRes.SWITCH), SENSOR(StringsRes.SENSOR), UNKNOWN(StringsRes.UNKNOWN);

    companion object {

        fun fromString(str: String?): SensorDomain = when (str?.lowercase()) {
            "switch" -> SWITCH
            "sensor" -> SENSOR
            else -> UNKNOWN
        }
    }
}

object SensorCardRes {
    val lightBulb = Res.drawable.light_bulb
    val thermometer = Res.drawable.thermometer
}