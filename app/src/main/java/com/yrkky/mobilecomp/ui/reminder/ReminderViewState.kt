package com.yrkky.mobilecomp.ui.reminder

import com.yrkky.core.domain.entity.Reminder
import kotlinx.coroutines.flow.Flow

sealed interface ReminderViewState {
    object Loading: ReminderViewState
    data class Success(
        val data: List<Reminder>
    ): ReminderViewState
}
