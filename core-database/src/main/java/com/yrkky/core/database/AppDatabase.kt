package com.yrkky.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yrkky.core.database.dao.ReminderDao
import com.yrkky.core.database.entity.ReminderEntity

@Database(
    entities = [ReminderEntity::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao

}