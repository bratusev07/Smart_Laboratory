package ru.bratusev.smartlab.data.core.repository

import ru.bratusev.smartlab.domain.core.model.NetworkStatus
import ru.bratusev.smartlab.domain.core.repository.NetworkRepository

expect class NetworkRepositoryImpl: NetworkRepository {
    override fun getNetworkStatus(): NetworkStatus
}