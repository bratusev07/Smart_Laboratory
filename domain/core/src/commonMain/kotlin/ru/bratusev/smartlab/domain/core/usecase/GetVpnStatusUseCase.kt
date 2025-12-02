package ru.bratusev.smartlab.domain.core.usecase

import ru.bratusev.smartlab.domain.core.repository.VpnRepository

class GetVpnStatusUseCase(
    private val vpnRepository: VpnRepository
) {
    operator fun invoke(): Boolean = vpnRepository.isVpnActive()
}
