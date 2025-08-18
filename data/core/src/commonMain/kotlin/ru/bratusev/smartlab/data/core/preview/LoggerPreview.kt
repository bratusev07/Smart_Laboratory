package ru.bratusev.smartlab.data.core.preview

expect class LoggerPreview {
    fun d(tag: String?, description: String)
    fun w(tag: String?, description: String)
    fun e(tag: String?, description: String)
}