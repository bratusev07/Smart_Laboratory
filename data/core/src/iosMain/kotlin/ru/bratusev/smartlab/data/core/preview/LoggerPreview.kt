package ru.bratusev.smartlab.data.core.preview

import platform.Foundation.NSLog

actual class LoggerPreview {
    actual fun d(tag: String?, description: String) {
        println("[$tag] $description")
    }

    actual fun w(tag: String?, description: String) {
        NSLog("[$tag] $description")
    }

    actual fun e(tag: String?, description: String) {
        NSLog("[$tag] $description")
    }
}