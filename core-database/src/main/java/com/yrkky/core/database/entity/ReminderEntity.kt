package com.yrkky.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "reminders",
    indices = [
        Index("reminderId", unique = true)
    ]
)

data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val reminderId: Long = 0,
    val message: String,
    val location_x: Double,
    val location_y: Double,
    val reminderTime: Long,
    val creationTime: Long,
    val creatorId: Long,
    val reminderSeen: Long,
)
