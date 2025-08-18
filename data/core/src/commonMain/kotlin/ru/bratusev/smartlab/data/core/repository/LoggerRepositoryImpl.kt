package ru.bratusev.smartlab.data.core.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import ru.bratusev.smartlab.data.core.Logger
import ru.bratusev.smartlab.data.core.database.LogcatMessageDao
import ru.bratusev.smartlab.data.core.getCurrentDateTime
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

    override suspend fun getLogMessages(logTypes: List<String>): List<LogcatMessage> {
        return listOf(
            LogcatMessage(title = "Title 1", description = "description ".repeat(20), time = "13:00:12", date = "13.06.2025", type = "d"),
            LogcatMessage(title = "Title 2", description = "description ".repeat(20), time = "13:00:12", date = "13.06.2025", type = "d"),
            LogcatMessage(title = "Title 3", description = "description ".repeat(20), time = "13:00:12", date = "13.06.2025", type = "e"),
            LogcatMessage(title = "Title 4", description = "description ".repeat(20), time = "13:00:12", date = "13.06.2025", type = "d"),
            LogcatMessage(title = "Title 5", description = "description ".repeat(20), time = "13:00:12", date = "13.06.2025", type = "e"),
            LogcatMessage(title = "Title 6", description = "description ".repeat(20), time = "13:00:12", date = "13.06.2025", type = "w"),
            LogcatMessage(title = "Title 7", description = "description ".repeat(20), time = "13:00:12", date = "13.06.2025", type = "e"),
            LogcatMessage(title = "Title 8", description = "description ".repeat(20), time = "13:00:12", date = "13.06.2025", type = "e"),
            LogcatMessage(title = "Title 9", description = "description ".repeat(20), time = "13:00:12", date = "13.06.2025", type = "w"),
            LogcatMessage(title = "Title 10", description = "description ".repeat(20), time = "13:00:12", date = "13.06.2025", type = "e"),
            LogcatMessage(title = "Title 11", description = "description ".repeat(20), time = "13:00:12", date = "13.06.2025", type = "e"),
            LogcatMessage(title = "Title 12", description = "description ".repeat(20), time = "13:00:12", date = "13.06.2025", type = "d"),
            LogcatMessage(title = "Title 13", description = "description ".repeat(20), time = "13:00:12", date = "13.06.2025", type = "d"),
            LogcatMessage(title = "Title 14", description = "description ".repeat(20), time = "13:00:12", date = "13.06.2025", type = "d"),
            LogcatMessage(title = "Title 15", description = "description ".repeat(20), time = "13:00:12", date = "13.06.2025", type = "d"),
        ).filter { logTypes.contains(it.type) }
    }
}