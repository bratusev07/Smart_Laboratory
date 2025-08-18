package ru.bratusev.smartlab.data.core.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LogcatMessages")
data class LogcatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String?,
    val description: String?,
    val time: String?,
    val date: String?,
    val type: String?
)
