package com.yrkky.core.domain.entity

import java.time.LocalDateTime

data class Reminder(
    val reminderId: Long = 0,
    val message: String,
    val location_x: Double,
    val location_y: Double,
    val reminderTime: Long,
    val creationTime: Long,
    val creatorId: Long,
    val reminderSeen: Long,

    )
