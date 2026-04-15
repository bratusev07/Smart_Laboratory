package ru.bratusev.smartlab.ui.core.components.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Battery2Bar
import androidx.compose.material.icons.filled.Battery3Bar
import androidx.compose.material.icons.filled.Battery6Bar
import androidx.compose.material.icons.filled.BatteryAlert
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.BatterySaver
import androidx.compose.material.icons.filled.BatteryStd
import androidx.compose.material.icons.filled.ConnectWithoutContact
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Usb
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material.icons.outlined.SolarPower
import smartlaboratory.ui.core.generated.resources.Res
import smartlaboratory.ui.core.generated.resources.dots_square
import smartlaboratory.ui.core.generated.resources.zigbee

fun getIconFromMdi(mdi: String): AppIcon {
    return when (mdi) {
        "mdi:dots-square" -> AppIcon.Resource(Res.drawable.dots_square)
        "mdi:zigbee" -> AppIcon.Resource(Res.drawable.zigbee)

        "mdi:home" -> AppIcon.Vector(Icons.Default.Home)
        "mdi:solar-panel-large" -> AppIcon.Vector(Icons.Outlined.SolarPower)
        "mdi:battery-30" -> AppIcon.Vector(Icons.Default.Battery3Bar)
        "mdi:battery-minus" -> AppIcon.Vector(Icons.Default.BatteryAlert)
        "mdi:battery" -> AppIcon.Vector(Icons.Default.BatteryStd)
        "mdi:wifi-off" -> AppIcon.Vector(Icons.Default.WifiOff)
        "mdi:map" -> AppIcon.Vector(Icons.Default.Map)
        "mdi:cellphone" -> AppIcon.Vector(Icons.Default.Smartphone)
        "mdi:battery-60" -> AppIcon.Vector(Icons.Default.Battery6Bar)
        "mdi:battery-20" -> AppIcon.Vector(Icons.Default.Battery2Bar)
        "mdi:human-greeting-proximit" -> AppIcon.Vector(Icons.Default.ConnectWithoutContact)
        "mdi:battery-charging-60" -> AppIcon.Vector(Icons.Default.BatteryChargingFull)
        "mdi:battery-plus" -> AppIcon.Vector(Icons.Default.BatterySaver)
        "mdi:usb-port" -> AppIcon.Vector(Icons.Default.Usb)
        "" -> AppIcon.Vector(Icons.Default.VisibilityOff)
        "null" -> AppIcon.Vector(Icons.Default.Minimize)
        else -> AppIcon.Vector(Icons.Default.Dangerous)
    }
}