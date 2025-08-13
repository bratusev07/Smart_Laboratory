package ru.bratusev.smartlab.data.core.repository.preview

import ru.bratusev.smartlab.data.core.preview.LoggerPreview
import ru.bratusev.smartlab.domain.core.model.LogcatMessage
import ru.bratusev.smartlab.domain.core.repository.LoggerRepository

class LoggerRepositoryPreview(private val logger: LoggerPreview) : LoggerRepository {
    override fun d(tag: String?, description: String) {
        logger.d(tag, description)
    }

    override fun w(tag: String?, description: String) {
        logger.d(tag, description)
    }

    override fun e(tag: String?, description: String) {
        logger.d(tag, description)
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