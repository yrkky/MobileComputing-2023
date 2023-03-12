package com.yrkky.mobilecomp.ui.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import com.yrkky.mobilecomp.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.insets.systemBarsPadding
import com.google.android.gms.maps.model.LatLng
import com.yrkky.core.domain.entity.Category
import com.yrkky.mobilecomp.ui.category.CategoryViewState
import com.yrkky.mobilecomp.ui.reminder.MainViewModel
import com.yrkky.mobilecomp.ui.reminder.ReminderList
import java.math.RoundingMode
import java.text.DecimalFormat

@Composable
fun Home(
    mainViewModel : MainViewModel = hiltViewModel(),
    modifier: Modifier,
    navigationController: NavController
) {
    val viewState by mainViewModel.categoryState.collectAsState()

    var longitude = remember { mutableStateOf(0.0)}
    var latitude = remember { mutableStateOf(0.0)}
    val filterNearby = remember { mutableStateOf(false) }

    val latlng = navigationController
        .currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<LatLng>("location_data")
        ?.value

    if (latlng != null) {
        longitude.value = latlng.longitude
        latitude.value = latlng.latitude
    }

    when (viewState) {
        is CategoryViewState.Success -> {
            val selectedCategory = (viewState as CategoryViewState.Success).selectedCategory
            val categories = (viewState as CategoryViewState.Success).data

            Surface(modifier = Modifier.fillMaxSize()) {
                HomeContent(
                    selectedCategory = selectedCategory!!,
                    categories = categories,
                    onCategorySelected = mainViewModel::onCategorySelected,
                    navigationController = navigationController,
                    mainViewModel = mainViewModel,
                    latitude = latitude,
                    longitude = longitude,
                    filterNearby = filterNearby
                )
            }
        }
        is CategoryViewState.Error -> {

        }
        is CategoryViewState.Loading -> {

        }
    }
}

@Composable
fun HomeContent(
    selectedCategory: Category,
    categories: List<Category>,
    onCategorySelected: (Category) -> Unit,
    navigationController: NavController,
    mainViewModel: MainViewModel,
    latitude: MutableState<Double>,
    longitude: MutableState<Double>,
    filterNearby: MutableState<Boolean>
) {
    val reminderState = remember { mutableStateOf("Passed") }

    Scaffold(
        modifier = Modifier.padding(bottom = 24.dp),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigationController.navigate(route = "reminder") },
                contentColor = Color.Blue,
                modifier = Modifier.padding(all = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }

    ) {
        Column(
            modifier = Modifier
                .systemBarsPadding()
                .fillMaxWidth()
        ) {
            val appBarColor = MaterialTheme.colors.secondary.copy(alpha = 0.87f)

            HomeAppBar(
                backgroundColor = appBarColor,
                navigationController = navigationController
            )

            CategoryTabs(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = onCategorySelected,
            )

            StateTabs(selectedState = reminderState)

            NearbyReminders(
                navigationController = navigationController,
                latitude = latitude,
                longitude = longitude,
                filterNearby = filterNearby,
            )

            ReminderList(
                selectedCategory = selectedCategory,
                mainViewModel = mainViewModel,
                navigationController = navigationController,
                selectedState = reminderState,
                latitude = latitude,
                longitude = longitude,
                filterNearby = filterNearby
            )

        }
    }
}

@Composable
private fun HomeAppBar(
    backgroundColor: Color,
    navigationController: NavController
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .heightIn(max = 24.dp)
            )
        },
        backgroundColor = backgroundColor,
        actions = {
            IconButton( onClick = {} ) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = stringResource(R.string.search))
            }
            IconButton( onClick = {navigationController.navigate("profile")} ) {
                Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = stringResource(R.string.account))
            }
        }
    )
}

@Composable
private fun CategoryTabs(
    categories: List<Category>,
    selectedCategory: Category,
    onCategorySelected: (Category) -> Unit
) {
    val selectedIndex = categories.indexOfFirst { it == selectedCategory }
    ScrollableTabRow(
        selectedTabIndex = selectedIndex,
        edgePadding = 24.dp,
        indicator = emptyTabIndicator,
        modifier = Modifier.fillMaxWidth(),
    ) {
        categories.forEachIndexed { index, category ->
            Tab(
                selected = index == selectedIndex,
                onClick = { onCategorySelected(category) }
            ) {
                ChoiceChipContent(
                    text = category.name,
                    selected = index == selectedIndex,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun StateTabs(
    selectedState: MutableState<String>
) {
    val tabs = listOf("All", "Upcoming", "Passed")
    val selectedIndex = tabs.indexOfFirst { it == selectedState.value }
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround) {
        Text(text = "State", textAlign = TextAlign.Center, modifier = Modifier.padding(20.dp))
        ScrollableTabRow(
            selectedTabIndex = selectedIndex,
            edgePadding = 24.dp,
            indicator = emptyTabIndicator,
            modifier = Modifier.fillMaxWidth(),
        ) {
            tabs.forEachIndexed { index, state ->
                Tab(
                    selected = index == selectedIndex,
                    onClick = { selectedState.value = state}
                ) {
                    ChoiceChipContent(
                        text = state,
                        selected = index == selectedIndex,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun NearbyReminders(
    navigationController: NavController,
    latitude: MutableState<Double>,
    longitude: MutableState<Double>,
    filterNearby: MutableState<Boolean>
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {}
    )

    val df = DecimalFormat("#.######")
    df.roundingMode = RoundingMode.HALF_UP

    Surface(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.fillMaxWidth()
            .height(75.dp)
    ) {

        Column(modifier = Modifier.padding(horizontal = 15.dp)) {

            Row {
                Text("Nearby Reminder location \n Lat: ${df.format(latitude.value)} Long: ${df.format(longitude.value)}")

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
                    modifier = Modifier.width(100.dp)
                ) {
                    Text(text = "Location Filter")
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly) {
                Text("Enable filter")
                Switch(
                    checked = filterNearby.value,
                    onCheckedChange = { filterNearby.value = it }
                )

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

@Composable
private fun ChoiceChipContent(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        color = when {
            selected -> MaterialTheme.colors.secondary.copy(alpha = 0.87f)
            else -> MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
        },
        contentColor = when {
            selected -> Color.Black
            else -> MaterialTheme.colors.onSurface
        },
        shape = MaterialTheme.shapes.small,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

private val emptyTabIndicator: @Composable (List<TabPosition>) -> Unit = {}
