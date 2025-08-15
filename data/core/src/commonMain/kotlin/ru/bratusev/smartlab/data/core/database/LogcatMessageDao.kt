package ru.bratusev.smartlab.data.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.bratusev.smartlab.data.core.model.LogcatMessageEntity

@Dao
interface LogcatMessageDao {

    @Insert
    suspend fun insert(item: LogcatMessageEntity)

    @Query("SELECT count(*) FROM LogcatMessages")
    suspend fun count(): Int

    @Query("SELECT * FROM LogcatMessages")
    fun getAllAsFlow(): Flow<List<LogcatMessageEntity>>
}