package ru.bratusev.smartlab.domain.core.usecase

import kotlinx.coroutines.flow.Flow
import ru.bratusev.smartlab.domain.core.model.socket.ServiceEntity
import ru.bratusev.smartlab.domain.core.repository.SocketRepository

class GetAreaDevicesUseCase(
    private val socketRepository: SocketRepository
) {
    operator fun invoke(areaId: String): Flow<List<ServiceEntity>> = socketRepository.observeAreaDevices(areaId)
}

