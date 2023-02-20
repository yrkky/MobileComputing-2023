package com.yrkky.mobilecomp.ui.reminder

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat.from
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yrkky.core.domain.entity.Category
import com.yrkky.core.domain.entity.Reminder
import com.yrkky.core.domain.repository.CategoryRepository
import com.yrkky.core.domain.repository.ReminderRepository
import com.yrkky.mobilecomp.Graph
import com.yrkky.mobilecomp.R
import com.yrkky.mobilecomp.ui.category.CategoryViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

private lateinit var editReminder: Reminder

@HiltViewModel
class MainViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val categoryRepository: CategoryRepository
): ViewModel() {

    private val _reminderViewState = MutableStateFlow<ReminderViewState>(ReminderViewState.Loading)
    val reminderState: StateFlow<ReminderViewState> = _reminderViewState

    private val _categoryList: MutableStateFlow<List<Category>> = MutableStateFlow(mutableListOf())
    val categories: StateFlow<List<Category>> =_categoryList

    private val _categoryViewState = MutableStateFlow<CategoryViewState>(CategoryViewState.Loading)
    val categoryState: StateFlow<CategoryViewState> = _categoryViewState

    private val _selectedCategory = MutableStateFlow<Category?>(null)

    fun saveReminder(Reminder: Reminder) {
        viewModelScope.launch {
            reminderRepository.addReminder(Reminder)
            notifyUserOfReminder(Reminder)
        }
    }

    fun setEditReminder(reminder: Reminder) {
        editReminder = reminder
    }

    fun getEditReminder() : Reminder{
        return editReminder
    }

    //fun getReminder(reminderId: Long) {
    //    viewModelScope.launch {
    //        reminderRepository.getOne(Reminder)
    //        notifyUserOfReminder(Reminder)
    //    }
    //}

    fun onCategorySelected(category: Category) {
        _selectedCategory.value = category
    }

    private fun notifyUserOfReminder(Reminder: Reminder) {
        val notificationId = 10
        val builder = NotificationCompat.Builder(
            Graph.appContext,
            "channel_id"
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("New Reminder made")
            .setContentText("Will remind ${Reminder.title} on ${Reminder.reminderTime}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(from(Graph.appContext)) {
            if (ActivityCompat.checkSelfPermission(
                    Graph.appContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(notificationId, builder.build())
        }
    }

    private fun createNotificationChannel() {
        val name = "NotificationChannel"
        val descriptionText = "NotificationChannelDescription"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("channel_id", name, importance).apply {
            description = descriptionText
        }
        val notificationManager = Graph.appContext
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun loadRemindersFor(category: Category?) {
        if (category != null) {
            viewModelScope.launch {
                val Reminders = reminderRepository.loadAllReminders()
                _reminderViewState.value =
                    ReminderViewState.Success(
                        Reminders.filter {
                            it.categoryId == category.categoryId }
                    )
            }
        }
    }

    private suspend fun loadCategories() {
        combine(
            categoryRepository.loadCategories()
                .onEach { categories ->
                    if (categories.isNotEmpty() && _selectedCategory.value == null) {
                        _selectedCategory.value = categories.first()
                    }
                },
            _selectedCategory
        ) { categories, selectedCategory ->
            _categoryViewState.value = CategoryViewState.Success(selectedCategory, categories)
            _categoryList.value = categories
        }
            .catch { error -> CategoryViewState.Error(error) }
            .launchIn(viewModelScope)
    }

    fun deleteReminder(reminder: Reminder) {
        viewModelScope.launch {
            reminderRepository.deleteReminder(reminder)
        }
    }

    private fun fakeData() = listOf(
        Category(name = "School"),
        Category(name = "Family"),
        Category(name = "Work"),
        Category(name = "Hobby"),
        Category(name = "House"),
        Category(name = "Misc"),
        Category(name = "Own Project 1"),
        Category(name = "Own Project 2"),
    )

    private fun dummyData() : List<Reminder> {
        return listOf(
            Reminder(
                title = "Wash dishes",
                categoryId = 1,
                message = "This is a description of a reminder",
                reminderTime = LocalDateTime.now(),
                creationTime = LocalDateTime.now(),
                location_x = 65.2514,
                location_y = 47.2541,
                creatorId = 1,
                reminderSeen = LocalDateTime.now(),
                icon = "error"
            ),
            Reminder(
                title = "Vacuum",
                categoryId = 2,
                message = "This is a description of a reminder",
                reminderTime = LocalDateTime.now(),
                creationTime = LocalDateTime.now(),
                location_x = 65.2514,
                location_y = 47.2541,
                creatorId = 1,
                reminderSeen = LocalDateTime.now(),
                icon = "warning",
            ),
            Reminder(
                title = "Laundry",
                categoryId = 3,
                message = "This is a description of a reminder",
                reminderTime = LocalDateTime.now(),
                creationTime = LocalDateTime.now(),
                location_x = 65.2514,
                location_y = 47.2541,
                creatorId = 1,
                reminderSeen = LocalDateTime.now(),
                icon = "important",
            ),
            Reminder(
                title = "Do homework",
                categoryId = 4,
                message = "This is a description of a reminder",
                reminderTime = LocalDateTime.now(),
                creationTime = LocalDateTime.now(),
                location_x = 65.2514,
                location_y = 47.2541,
                creatorId = 1,
                reminderSeen = LocalDateTime.now(),
                icon = "star",
            )
        )
    }

    init {
        createNotificationChannel()

        fakeData().forEach {
            viewModelScope.launch {
                categoryRepository.addCategory(it)
            }
        }
        dummyData().forEach {
            viewModelScope.launch {
                saveReminder(it)
            }
        }
        viewModelScope.launch {
            loadCategories()
        }
    }
}

