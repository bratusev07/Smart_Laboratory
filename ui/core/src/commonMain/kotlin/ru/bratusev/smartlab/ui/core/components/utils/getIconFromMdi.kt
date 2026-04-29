package ru.bratusev.smartlab.ui.core.components.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.ui.graphics.vector.ImageVector
import ru.bratusev.smartlab.ui.core.generated.icons.mdi.icons.Battery20Mdi
import ru.bratusev.smartlab.ui.core.generated.icons.mdi.icons.Battery30Mdi
import ru.bratusev.smartlab.ui.core.generated.icons.mdi.icons.Battery60Mdi
import ru.bratusev.smartlab.ui.core.generated.icons.mdi.icons.BatteryCharging60Mdi
import ru.bratusev.smartlab.ui.core.generated.icons.mdi.icons.BatteryMdi
import ru.bratusev.smartlab.ui.core.generated.icons.mdi.icons.BatteryMinusMdi
import ru.bratusev.smartlab.ui.core.generated.icons.mdi.icons.BatteryPlusMdi
import ru.bratusev.smartlab.ui.core.generated.icons.mdi.icons.CellphoneMdi
import ru.bratusev.smartlab.ui.core.generated.icons.mdi.icons.DotsSquareMdi
import ru.bratusev.smartlab.ui.core.generated.icons.mdi.icons.HomeMdi
import ru.bratusev.smartlab.ui.core.generated.icons.mdi.icons.HumanGreetingProximityMdi
import ru.bratusev.smartlab.ui.core.generated.icons.mdi.icons.MapMdi
import ru.bratusev.smartlab.ui.core.generated.icons.mdi.icons.SolarPanelLargeMdi
import ru.bratusev.smartlab.ui.core.generated.icons.mdi.icons.UsbPortMdi
import ru.bratusev.smartlab.ui.core.generated.icons.mdi.icons.WifiOffMdi
import ru.bratusev.smartlab.ui.core.generated.icons.mdi.icons.ZigbeeMdi
import ru.bratusev.smartlab.ui.core.generated.icons.mdi.Icons as Mdi

private val mdiVectorMap: Map<String, ImageVector> = mapOf(
    "mdi:usb-port" to Mdi.UsbPortMdi
)

fun getIconFromMdi(mdi: String): AppIcon {
    val generatedVector = mdiVectorMap[mdi]
    if (generatedVector != null) {
        return AppIcon.Vector(generatedVector)
    }

    when (mdi) {
        "" -> return AppIcon.Vector(Icons.Default.VisibilityOff)
        "null" -> return AppIcon.Vector(Icons.Default.Minimize)
    }

    val cleanName = mdi.removePrefix("mdi:")

    val url = "https://cdn.jsdelivr.net/npm/@mdi/svg@latest/svg/$cleanName.svg"

    println("Result image url: $url")

    return AppIcon.Url(url)
}