package ru.bratusev.smartlab.domain.core.repository

interface VpnRepository {
    fun isVpnActive(): Boolean
}