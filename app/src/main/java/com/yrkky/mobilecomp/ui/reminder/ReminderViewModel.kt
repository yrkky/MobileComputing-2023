package com.yrkky.mobilecomp.ui.reminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yrkky.core.domain.entity.Reminder
import com.yrkky.core.domain.repository.ReminderRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository
): ViewModel() {

        fun saveReminder(reminder: Reminder) {
            viewModelScope.launch {
                reminderRepository.addReminder(reminder)
            }

        }

}