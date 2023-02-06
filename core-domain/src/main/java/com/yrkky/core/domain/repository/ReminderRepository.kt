package com.yrkky.core.domain.repository

import com.yrkky.core.domain.entity.Reminder

interface ReminderRepository {
    suspend fun addReminder(reminder: Reminder)

}