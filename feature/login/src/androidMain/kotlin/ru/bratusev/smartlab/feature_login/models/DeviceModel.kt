package ru.bratusev.smartlab.feature_login.models

import android.os.Build
import android.content.Context
import android.provider.Settings

actual class Device(context: Context) {
    actual val appId: String = context.packageName
    actual val appName: String = context.applicationInfo.loadLabel(context.packageManager).toString()
    actual val appVersion: String = try {
        val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        "${'$'}{pInfo.versionName} (${pInfo.versionCode})"
    } catch (e: Exception) {
        "Unknown"
    }
    actual val deviceName: String = Settings.Global.getString(context.contentResolver, Settings.Global.DEVICE_NAME) ?: Build.MODEL
    actual val manufacturer: String = Build.MANUFACTURER
    actual val model: String = Build.MODEL
    actual val osName: String = "Android"
    actual val osVersion: String = Build.VERSION.RELEASE ?: "Unknown"
    actual val deviceId: String = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: "Unknown"
}
