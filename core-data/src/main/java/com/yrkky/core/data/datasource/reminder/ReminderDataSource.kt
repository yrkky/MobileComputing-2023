package com.yrkky.core.data.datasource.reminder

import com.yrkky.core.domain.entity.Reminder

interface ReminderDataSource {
    suspend fun addReminder(reminder: Reminder)


}