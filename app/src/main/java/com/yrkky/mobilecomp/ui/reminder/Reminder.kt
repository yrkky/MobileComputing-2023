package com.yrkky.mobilecomp.ui.reminder

import android.Manifest
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
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.yrkky.core.domain.entity.Category
import com.yrkky.mobilecomp.ui.utils.GeofenceUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList


@Composable
fun Reminder(
    navigationController: NavController,
    viewModel: MainViewModel = hiltViewModel(),
) {

    val context = LocalContext.current
    val reminderTitle = remember { mutableStateOf("") }
    val reminderMessage = remember { mutableStateOf("") }
    val reminderCategory = remember { mutableStateOf("") }
    val reminderTime = remember { mutableStateOf("") }
    val reminderIcon = remember { mutableStateOf("")}
    val shouldNotify = remember { mutableStateOf(true) }
    val geofencer = remember { GeofenceUtils(context)}
    var longitude = remember { mutableStateOf(0.0)}
    var latitude = remember { mutableStateOf(0.0)}
    var offset by remember { mutableStateOf(0f) }

    val latlng = navigationController
        .currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<LatLng>("location_data")
        ?.value

    if (latlng != null) {
        longitude.value = latlng.longitude
        latitude.value = latlng.latitude
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {}
    )

    fun buttonEnabled(): Boolean {
        return reminderTitle.value.isNotEmpty() && reminderCategory.value.isNotEmpty()
    }

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .scrollable(
                    state = rememberScrollableState { delta ->
                        offset += delta
                        delta
                        },
                    orientation = Orientation.Vertical)
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
                    label = { Text(stringResource(R.string.message)) },
                    shape = RoundedCornerShape(corner = CornerSize(10.dp))
                )

                Spacer(modifier = Modifier.height(10.dp))

                CategoryListDropdown(
                    viewModel = viewModel,
                    category = reminderCategory
                )

                Spacer(modifier = Modifier.height(10.dp))

                smallIconDropdown(viewModel, reminderIcon)

                Spacer(modifier = Modifier.height(10.dp))

                DatePicker(
                    remindtime = reminderTime,
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.height(20.dp)
                ) {
                    Text(text = "Enable notifications")
                    Switch(
                        checked = shouldNotify.value,
                        onCheckedChange = { shouldNotify.value = it }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LocationPicker(latlng)
                OutlinedButton(
                    onClick = {
                        requestPermission(
                            context = context,
                            permission = Manifest.permission.ACCESS_FINE_LOCATION,
                            requestPermission = { launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION) }
                        ).apply {
                            navigationController.navigate("map")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Reminder location")
                }

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

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            requestPermission(
                                context = context,
                                permission = Manifest.permission.ACCESS_FINE_LOCATION,
                                requestPermission = { launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION) }
                            )
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            requestPermission(
                                context = context,
                                permission = Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                                requestPermission = { launcher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION) }
                            )
                        }

                        viewModel.saveReminder(
                            Reminder(
                                title = reminderTitle.value,
                                message = reminderMessage.value,
                                location_x = latitude.value,
                                location_y = longitude.value,
                                reminderTime = LocalDateTime.parse(reminderTime.value),
                                creationTime = LocalDateTime.now(),
                                creatorId = 1,
                                categoryId = getCategoryId(viewModel, reminderCategory.value),
                                reminderSeen = LocalDateTime.now(),
                                icon = reminderIcon.value,
                            ),
                            notify = shouldNotify.value
                        )
                        navigationController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.save))
                }
            }
        }
    }
}

@Composable
private fun LocationPicker(
    latlng: LatLng?
) {

    val latitude = remember { mutableStateOf("") }
    val longitude = remember { mutableStateOf("") }

    if (latlng == null) {
        latitude.value = "No Location Set"
        longitude.value = "No Location Set"
    } else {
        latitude.value = latlng.latitude.toString()
        longitude.value = latlng.longitude.toString()
    }

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly) {
        OutlinedTextField(
            modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp / 100 * 45).padding(5.dp),
            value = latitude.value.substring(0, latitude.value.length - 5),
            onValueChange = { latitude_ ->
                latitude.value = latitude_
            },
            label = { Text(stringResource(R.string.latitude)) },
            shape = RoundedCornerShape(corner = CornerSize(10.dp)),
            readOnly = true
        )

        OutlinedTextField(
            modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp / 100 * 45).padding(5.dp),
            value = longitude.value.substring(0, latitude.value.length - 5),
            onValueChange = { longitude_ ->
                longitude.value = longitude_
            },
            label = { Text(stringResource(R.string.longitude)) },
            shape = RoundedCornerShape(corner = CornerSize(10.dp)),
            readOnly = true
        )

    }
}

private fun getCategoryId(viewModel: MainViewModel, categoryName: String): Long {
    return viewModel.categories.value.first { it.name.lowercase() == categoryName.lowercase() }.categoryId
}

@Composable
private fun CategoryListDropdown(
    viewModel: MainViewModel,
    category: MutableState<String>
) {
    val categoryState = viewModel.categories.collectAsState()
    var categoryState_withoutAll = categoryState.value.toMutableSet()
    Log.i("CategoryListDropdown", "categorylist: ${categoryState}")
    categoryState_withoutAll.remove(Category(categoryId=1, name="All"))
    Log.i("CategoryListDropdown", "categorylist_withoutAll: ${categoryState_withoutAll}")


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
            categoryState_withoutAll.forEach { dropDownOption ->
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

    }

}

@Composable
private fun smallIconDropdown(
    viewModel: MainViewModel,
    reminderIcon: MutableState<String>
) {

    var expanded by remember { mutableStateOf(false) }
    val icon = if (expanded) {
        Icons.Outlined.ArrowDropUp // requires androidx.compose.material:material-icons-extended dependency
    } else {
        Icons.Filled.ArrowDropDown
    }

    val listIcons = ArrayList<String>()
    listIcons.add("immediate")
    listIcons.add("urgent")
    listIcons.add("important")
    listIcons.add("late")
    listIcons.add("incomplete")
    listIcons.add("new")


    fun nameToIcon(name: String) : ImageVector {
        return when (name) {
            "immediate" -> {
                Icons.Outlined.Error
            }
            "urgent" -> {
                Icons.Outlined.Warning
            }
            "late" -> {
                Icons.Outlined.AssignmentLate
            }
            "important" -> {
                Icons.Outlined.LabelImportant
            }
            "incomplete" -> {
                Icons.Outlined.StarHalf
            }
            "new" -> {
                Icons.Outlined.StarOutline
            }
            else -> {
                Icons.Outlined.StarOutline
            }
        }
    }


    Column {
        OutlinedTextField(
            value = reminderIcon.value,
            onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Icon") },
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
            listIcons.forEach { dropDownOption ->
                DropdownMenuItem(
                    onClick = {
                        reminderIcon.value = dropDownOption
                        expanded = false
                    }
                ) {
                    Text(text = dropDownOption)
                    Icon(imageVector = nameToIcon(dropDownOption),
                    contentDescription = null,
                    )
                }
            }
        }
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