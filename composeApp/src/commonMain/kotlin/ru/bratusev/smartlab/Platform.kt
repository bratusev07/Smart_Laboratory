package ru.bratusev.smartlab

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform