package ru.bratusev.smartlab.domain.core.repository

interface LoggerRepository {
    fun d(tag: String?, description: String)
    fun w(tag: String?, description: String)
    fun e(tag: String?, description: String)
}