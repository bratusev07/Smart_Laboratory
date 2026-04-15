package ru.bratusev.smartlab.ui.core.models.sensorCard

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.StringResource
import ru.bratusev.smartlab.ui.core.components.utils.AppIcon
import ru.bratusev.smartlab.ui.core.components.utils.getIconFromMdi
import ru.bratusev.smartlab.ui.core.theme.SensorCardCommonColors
import smartlaboratory.ui.core.generated.resources.Res
import smartlaboratory.ui.core.generated.resources.light_bulb
import smartlaboratory.ui.core.generated.resources.sensor
import smartlaboratory.ui.core.generated.resources.sensor_state_disabled
import smartlaboratory.ui.core.generated.resources.sensor_state_off
import smartlaboratory.ui.core.generated.resources.sensor_state_on
import smartlaboratory.ui.core.generated.resources.sensor_state_value
import smartlaboratory.ui.core.generated.resources.switch_label
import smartlaboratory.ui.core.generated.resources.thermometer
import smartlaboratory.ui.core.generated.resources.unknown

sealed class SensorCardUi {

    abstract val id: String
    abstract val state: SensorState
    abstract val domain: SensorDomain
    abstract val mdiIcon: String
    abstract val tints: SensorCardTints

    abstract val icon: AppIcon

    data class Row(
        val title: String,
        override val id: String,
        override val state: SensorState,
        override val domain: SensorDomain,
        override val mdiIcon: String,
        override val tints: SensorCardTints,
    ) : SensorCardUi() {
        override val icon: AppIcon = getIconFromMdi(mdiIcon)
    }

    sealed class Tile : SensorCardUi() {
        class Small(
            override val id: String,
            override val state: SensorState,
            override val domain: SensorDomain,
            override val mdiIcon: String,
            override val tints: SensorCardTints,
        ) : Tile() {
            override val icon: AppIcon = getIconFromMdi(mdiIcon)
        }

        data class Medium(
            val title: String,
            override val id: String,
            override val state: SensorState,
            override val domain: SensorDomain,
            override val mdiIcon: String,
            override val tints: SensorCardTints,
        ) : Tile() {
            override val icon: AppIcon = getIconFromMdi(mdiIcon)
        }

        data class Large(
            val title: String,
            val description: String,
            override val id: String,
            override val state: SensorState,
            override val domain: SensorDomain,
            override val mdiIcon: String,
            override val tints: SensorCardTints,
        ) : Tile() {
            override val icon: AppIcon = getIconFromMdi(mdiIcon)
        }

        data class Sensor(
            val title: String,
            val measurementUnit: String,
            override val id: String,
            override val state: SensorState.SensorValue,
            override val domain: SensorDomain,
            override val mdiIcon: String,
            override val tints: SensorCardTints,
        ) : Tile() {
            override val icon: AppIcon = getIconFromMdi(mdiIcon)
        }
    }

    sealed class Widget : SensorCardUi() {
        data class Switch(
            val title: String,
            override val id: String,
            override val state: SensorState,
            override val domain: SensorDomain,
            override val mdiIcon: String,
            override val tints: SensorCardTints,
        ) : Widget() {
            override val icon: AppIcon = getIconFromMdi(mdiIcon)
        }
    }

    data class Modal(
        val title: String?,
        override val id: String,
        override val state: SensorState,
        override val domain: SensorDomain,
        override val mdiIcon: String,
        override val tints: SensorCardTints = SensorCardTints.SingleColor(Color.Gray),
    ) : SensorCardUi() {
        override val icon: AppIcon = getIconFromMdi(mdiIcon)
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

}

sealed class SensorState(val localeNameRes: StringResource) {
    data object On : SensorState(Res.string.sensor_state_on)
    data object Off : SensorState(Res.string.sensor_state_off)
    data object Unavailable : SensorState(Res.string.sensor_state_disabled)
    data class SensorValue(val value: Float) : SensorState(Res.string.sensor_state_value) {
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

enum class SensorDomain(val nameResource: StringResource) {
    SWITCH(Res.string.switch_label), SENSOR(Res.string.sensor), UNKNOWN(Res.string.unknown);

    companion object {

        fun fromString(str: String?): SensorDomain = when (str?.lowercase()) {
            "switch" -> SWITCH
            "sensor", "number" -> SENSOR
            else -> UNKNOWN
        }
    }
}

object SensorCardRes {
    val lightBulb = Res.drawable.light_bulb
    val thermometer = Res.drawable.thermometer
}

