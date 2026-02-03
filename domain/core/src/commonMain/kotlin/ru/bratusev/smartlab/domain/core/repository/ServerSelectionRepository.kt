package ru.bratusev.smartlab.domain.core.repository

import kotlinx.coroutines.flow.Flow
import ru.bratusev.smartlab.domain.core.model.ServerSelection

interface ServerSelectionRepository {
    fun getServerSelection(): Flow<ServerSelection>
    suspend fun setServerSelection(serverSelection: ServerSelection)
}