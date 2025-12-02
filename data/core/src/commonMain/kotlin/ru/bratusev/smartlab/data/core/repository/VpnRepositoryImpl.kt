package ru.bratusev.smartlab.data.core.repository

import ru.bratusev.smartlab.domain.core.repository.VpnRepository

expect class VpnRepositoryImpl: VpnRepository {
    override fun isVpnActive(): Boolean
}