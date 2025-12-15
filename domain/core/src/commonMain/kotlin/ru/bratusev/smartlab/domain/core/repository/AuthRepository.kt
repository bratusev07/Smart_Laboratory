package ru.bratusev.smartlab.domain.core.repository

import kotlinx.coroutines.flow.Flow
import ru.bratusev.smartlab.domain.core.model.Device

interface AuthRepository {

    suspend fun login(login: String, password: String)
    suspend fun config(token: String)
    suspend fun registrations(token: String, device: Device)
    suspend fun getToken(): String?

    suspend fun subscribeToken(): Flow<String>
}