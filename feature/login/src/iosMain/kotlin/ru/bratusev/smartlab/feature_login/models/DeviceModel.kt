package ru.bratusev.smartlab.feature_login.models

import platform.Foundation.*
import platform.UIKit.UIDevice

actual class Device {
    actual val appId: String = NSBundle.mainBundle.bundleIdentifier ?: "Unknown"
    actual val appName: String = NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleName") as? String ?: "Unknown"
    actual val appVersion: String = NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String ?: "Unknown"
    actual val deviceName: String = UIDevice.currentDevice.name
    actual val manufacturer: String = "Apple"
    actual val model: String = UIDevice.currentDevice.model
    actual val osName: String = UIDevice.currentDevice.systemName
    actual val osVersion: String = UIDevice.currentDevice.systemVersion
    actual val deviceId: String = UIDevice.currentDevice.identifierForVendor?.UUIDString ?: "Unknown"
} 