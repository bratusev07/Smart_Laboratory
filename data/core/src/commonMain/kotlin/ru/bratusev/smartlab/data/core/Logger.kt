package ru.bratusev.smartlab.data.core

expect class Logger {
    fun d(tag: String?, description: String)
    fun w(tag: String?, description: String)
    fun e(tag: String?, description: String)
}