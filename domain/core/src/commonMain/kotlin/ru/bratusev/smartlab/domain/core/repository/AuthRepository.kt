package ru.bratusev.smartlab.domain.core.repository

import ru.bratusev.smartlab.domain.core.model.Device

interface AuthRepository {

    suspend fun login(login: String, password: String) : String
    suspend fun config(token: String)
    suspend fun registrations(token: String, device: Device)

    suspend fun saveToken(token: String)

    suspend fun getToken(): String?
}