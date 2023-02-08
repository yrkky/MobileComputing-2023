package com.yrkky.mobilecomp.ui.reminder

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.insets.systemBarsPadding
import com.yrkky.mobilecomp.R
import com.yrkky.core.domain.entity.Reminder
import java.time.LocalDateTime

@Composable
fun Reminder(
    navigationController: NavController,
    onBackPress: () -> Unit,
    viewModel: ReminderViewModel
) {

    val reminderName = remember { mutableStateOf("") }

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            TopAppBar {
                IconButton(
                    onClick = onBackPress
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
                    value = reminderName.value,
                    onValueChange = { remindername -> reminderName.value = remindername },
                    label = { Text(stringResource(R.string.name)) },
                    shape = RoundedCornerShape(corner = CornerSize(10.dp))
                )
                Spacer(modifier = Modifier.height(10.dp))



                Button(
                    onClick = {
                        //navigationController.navigate("home")
                              viewModel.saveReminder(
                                  Reminder(
                                      message = reminderName.value,
                                      location_x = 0.0,
                                      location_y = 0.0,
                                      reminderTime = 1236312,
                                      creationTime = 1253463,
                                      creatorId = 1,
                                      reminderSeen = 12532,
                                      //date = LocalDateTime()
                                  )
                              )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.save))
                }
            }
        }
    }
}