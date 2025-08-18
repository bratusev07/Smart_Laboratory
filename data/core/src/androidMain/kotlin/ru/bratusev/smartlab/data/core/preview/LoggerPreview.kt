package ru.bratusev.smartlab.data.core.preview

import android.util.Log

actual class LoggerPreview {
    actual fun d(tag: String?, description: String) {
        Log.d(tag, description)
    }

    actual fun w(tag: String?, description: String) {
        Log.w(tag, description)
    }

    actual fun e(tag: String?, description: String) {
        Log.e(tag, description)
    }


}