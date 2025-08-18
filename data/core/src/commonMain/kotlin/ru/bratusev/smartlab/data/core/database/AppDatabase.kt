package ru.bratusev.smartlab.data.core.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import ru.bratusev.smartlab.data.core.model.LogcatMessageEntity

@Database(entities = [LogcatMessageEntity::class], version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun logcatMessagesDao(): LogcatMessageDao

    companion object {
        val DB_NAME: String = "smart-lab.db"
    }
}