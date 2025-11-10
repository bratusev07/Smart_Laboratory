package ru.bratusev.smartlab.data.core.local_storage.database

import androidx.room.RoomDatabaseConstructor

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor: RoomDatabaseConstructor<AppDatabase>{
    override fun initialize(): AppDatabase
}