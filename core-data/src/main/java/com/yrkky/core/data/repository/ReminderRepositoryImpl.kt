package com.yrkky.core.data.repository

import com.yrkky.core.data.datasource.reminder.ReminderDataSource
import com.yrkky.core.domain.entity.Reminder
import com.yrkky.core.domain.repository.ReminderRepository

class ReminderRepositoryImpl(
    private val reminderDataSource: ReminderDataSource
): ReminderRepository {
    override suspend fun addReminder(reminder: Reminder) {
        reminderDataSource.addReminder(reminder)
    }

}