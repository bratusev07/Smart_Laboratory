package ru.bratusev.smartlab.domain.core.repository

import ru.bratusev.smartlab.domain.core.model.NetworkStatus

interface NetworkRepository {
    fun getNetworkStatus(): NetworkStatus
}