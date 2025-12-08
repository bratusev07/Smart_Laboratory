package ru.bratusev.smartlab.domain.core.usecase

import ru.bratusev.smartlab.domain.core.model.NetworkStatus
import ru.bratusev.smartlab.domain.core.repository.NetworkRepository

class GetNetworkStatusUseCase(
    private val networkRepository: NetworkRepository
) {
    operator fun invoke(): NetworkStatus = networkRepository.getNetworkStatus()
}
