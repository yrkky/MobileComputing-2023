package com.yrkky.core.data.datasource.reminder

import com.yrkky.core.database.dao.ReminderDao
import com.yrkky.core.database.entity.ReminderEntity
import com.yrkky.core.domain.entity.Reminder
import java.time.LocalDateTime
import javax.inject.Inject

class ReminderDataSourceImpl @Inject constructor(
    private val reminderDao: ReminderDao
) : ReminderDataSource {

    override suspend fun addReminder(reminder: Reminder) {
        reminderDao.insertOrUpdate(reminder.toEntity())
    }

    private fun Reminder.toEntity() = ReminderEntity(
        reminderId = this.reminderId,
        message = this.message,
        location_x = this.location_x,
        location_y = this.location_y,
        reminderTime = this.reminderTime,
        creationTime = this.creationTime,
        creatorId = this.creatorId,
        reminderSeen = this.reminderSeen,
    )

    private fun ReminderEntity.fromEntity() = Reminder(
        reminderId = this.reminderId,
        message = this.message,
        location_x = this.location_x,
        location_y = this.location_y,
        reminderTime = this.reminderTime,
        creationTime = this.creationTime,
        creatorId = this.creatorId,
        reminderSeen = this.reminderSeen,
    )

}