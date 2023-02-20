package com.yrkky.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yrkky.core.database.dao.CategoryDao
import com.yrkky.core.database.dao.ReminderDao
import com.yrkky.core.database.entity.CategoryEntity
import com.yrkky.core.database.entity.ReminderEntity
import com.yrkky.core.database.utils.Converter

@Database(
    entities = [ReminderEntity::class, CategoryEntity::class],
    version = 1,
    //exportSchema = false
)

@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
    abstract fun categoryDao(): CategoryDao
}