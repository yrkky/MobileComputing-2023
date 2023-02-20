package com.yrkky.mobilecomp.ui.reminder

import androidx.compose.material.icons.outlined.Star
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.accompanist.insets.systemBarsPadding
import androidx.hilt.navigation.compose.hiltViewModel
import com.yrkky.mobilecomp.R
import com.yrkky.core.domain.entity.Reminder
import java.time.LocalDateTime
import java.util.*
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun EditReminder(
    navigationController: NavController,
    viewModel: MainViewModel = hiltViewModel(),
) {

    val reminder = viewModel.getEditReminder() //collectAsState()

    val reminderTitle = remember { mutableStateOf(reminder.title) }
    val reminderMessage = remember { mutableStateOf(reminder.message) }
    val reminderCategory = remember { mutableStateOf(reminder.categoryId) }
    val reminderTime = remember { mutableStateOf(reminder.reminderTime.toString()) }
    val reminderIcon = remember { mutableStateOf(reminder.icon) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {}
    )
    val context = LocalContext.current

    fun buttonEnabled(): Boolean {
        return reminderTitle.value.isNotEmpty() &&
                reminderTime.value.isNotEmpty()
    }

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            TopAppBar {
                IconButton(
                    onClick = { navigationController.navigate("home") }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                }
                Text(text = "Reminder")
            }
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(16.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = reminderTitle.value,
                    onValueChange = { reminderTitle_ -> reminderTitle.value = reminderTitle_ },
                    label = { Text(stringResource(R.string.name)) },
                    shape = RoundedCornerShape(corner = CornerSize(10.dp))
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = reminderMessage.value,
                    onValueChange = { reminderMessage_ -> reminderMessage.value = reminderMessage_ },
                    label = { Text(stringResource(R.string.name)) },
                    shape = RoundedCornerShape(corner = CornerSize(10.dp))
                )

                Spacer(modifier = Modifier.height(10.dp))

                DatePicker(
                    remindtime = reminderTime,
                )

                Spacer(modifier = Modifier.height(10.dp))


                Button(
                    enabled = buttonEnabled(),
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            requestPermission(
                                context = context,
                                permission = Manifest.permission.POST_NOTIFICATIONS,
                                requestPermission = { launcher.launch(Manifest.permission.POST_NOTIFICATIONS) }
                            )
                        }
                        viewModel.saveReminder(
                            Reminder(
                                title = reminderTitle.value,
                                message = reminderMessage.value,
                                location_x = 0.0,
                                location_y = 0.0,
                                reminderTime = LocalDateTime.parse(reminderTime.value),
                                creationTime = LocalDateTime.now(),
                                creatorId = 1,
                                categoryId = reminderCategory.value,
                                reminderSeen = LocalDateTime.now(),
                                icon = reminderIcon.value,
                            )
                        )
                        navigationController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.save))
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    enabled = buttonEnabled(),
                    onClick = {
                        viewModel.deleteReminder(reminder)
                        navigationController.navigate("home")
                    }
                ) {Text("Delete Reminder")}
            }
        }
    }
}

private fun getCategoryId(viewModel: MainViewModel, categoryName: String): Long {
    return viewModel.categories.value.first { it.name.lowercase() == categoryName.lowercase() }.categoryId
}

private fun getCategoryName(viewModel: MainViewModel, categoryId: Long): String {
    return viewModel.categories.value.first { it.categoryId == categoryId }.name
}

@Composable
private fun CategoryListDropdown(
    viewModel: MainViewModel,
    category: MutableState<String>
) {
    val categoryState = viewModel.categories.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    val icon = if (expanded) {
        Icons.Outlined.ArrowDropUp // requires androidx.compose.material:material-icons-extended dependency
    } else {
        Icons.Filled.ArrowDropDown
    }

    Column {
        OutlinedTextField(
            value = category.value,
            onValueChange = { category.value = it},
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Category") },
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            categoryState.value.forEach { dropDownOption ->
                DropdownMenuItem(
                    onClick = {
                        category.value = dropDownOption.name
                        expanded = false
                    }
                ) {
                    Text(text = dropDownOption.name)
                }
            }
        }
    }
}

@Composable
private fun DatePicker(
    remindtime: MutableState<String>
){
    val localContext = LocalContext.current
    val mDate = remember { mutableStateOf("") }
    val mTime = remember { mutableStateOf("") }
    val mDatetime = remember { mutableStateOf("")}

    val mYear: Int
    val mMonth: Int
    val mDay: Int
    val mHour: Int
    val mMinute: Int
    val mCalendar = Calendar.getInstance()

    mYear = mCalendar[Calendar.YEAR]
    mMonth = mCalendar[Calendar.MONTH]
    mDay = mCalendar[Calendar.DAY_OF_MONTH]
    mHour = mCalendar[Calendar.HOUR_OF_DAY]
    mMinute = mCalendar[Calendar.MINUTE]
    mCalendar.time = Date()

    val mDatePickerDialog = DatePickerDialog(
        localContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "$mDayOfMonth.${mMonth+1}.$mYear"
        }, mYear, mMonth, mDay
    )

    val mTimePickerDialog = TimePickerDialog(
        localContext,
        {_, mHour : Int, mMinute: Int ->
            mTime.value = "$mHour.$mMinute"
        }, mHour, mMinute, true
    )

    mDatetime.value = mDate.value + " " + mTime.value
    // LocalDateTime.of(mYear, mMonth, mDay, mHour, mMinute).toString()

    if (mDate.value.isNotEmpty() && mTime.value.isNotEmpty()) {
        remindtime.value = LocalDateTime.of(
            mDate.value.split(".")[2].toInt(),
            mDate.value.split(".")[1].toInt(),
            mDate.value.split(".")[0].toInt(),
            mTime.value.split(".")[0].toInt(),
            mTime.value.split(".")[1].toInt(),
        ).toString()

        mDatetime.value = LocalDateTime
            .parse(remindtime.value)
            .format(DateTimeFormatter
                .ofPattern("dd.MM.yyyy HH.mm")
            )
    }

    Column(modifier = Modifier.fillMaxWidth()
    ) {

        OutlinedTextField(
            value = mDatetime.value,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Date&Time") },
            onValueChange = { mDatetime.value = it},
            readOnly = true,
        )

        Button(onClick = {
            mTimePickerDialog.show()
            mDatePickerDialog.show()
        }) {
            Text(text = "Pick Date & Time")
        }

        Spacer(modifier = Modifier.size(15.dp))
    }
}

private fun requestPermission(
    context: Context,
    permission: String,
    requestPermission: () -> Unit
) {
    if (ContextCompat.checkSelfPermission(
            context,
            permission
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        requestPermission()
    }
}