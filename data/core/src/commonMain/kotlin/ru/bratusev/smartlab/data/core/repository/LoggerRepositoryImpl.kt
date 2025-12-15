package ru.bratusev.smartlab.data.core.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.bratusev.smartlab.data.core.Logger
import ru.bratusev.smartlab.data.core.getCurrentDateTime
import ru.bratusev.smartlab.data.core.local_storage.database.LogcatMessageDao
import ru.bratusev.smartlab.data.core.model.LogcatMessageEntity
import ru.bratusev.smartlab.domain.core.model.LogcatMessage
import ru.bratusev.smartlab.domain.core.repository.LoggerRepository

class LoggerRepositoryImpl(
    private val logger: Logger,
    private val logcatMessageDao: LogcatMessageDao,
    private val coroutineScope: CoroutineScope
) : LoggerRepository {

    override fun d(tag: String?, description: String) {
        logger.d(tag, description)
        insertLogMessage(tag, description, "d")
    }

    override fun w(tag: String?, description: String) {
        logger.w(tag, description)
        insertLogMessage(tag, description, "w")
    }

    override fun e(tag: String?, description: String) {
        logger.e(tag, description)
        insertLogMessage(tag, description, "e")
    }

    private fun insertLogMessage(tag: String?, description: String, type: String) {
        val (date, time) = getCurrentDateTime()
        val message = LogcatMessageEntity(
            title = tag,
            description = description,
            time = time,
            date = date,
            type = type
        )
        coroutineScope.launch(Dispatchers.IO) {
            logcatMessageDao.insert(message)
        }
    }

    override suspend fun getLogMessages(logTypes: List<String>): Flow<List<LogcatMessage>> {
        return logcatMessageDao.getAllByTypesAsFlow(logTypes)
            .map { messages ->
                messages.map { msg ->
                    LogcatMessage(
                        title = msg.title,
                        description = msg.description,
                        time = msg.time,
                        date = msg.date,
                        type = msg.type
                    )
                }
            }
            .flowOn(Dispatchers.IO)
    }
}