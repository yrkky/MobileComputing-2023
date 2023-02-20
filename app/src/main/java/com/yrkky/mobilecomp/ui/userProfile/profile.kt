package com.yrkky.mobilecomp.ui.userProfile

import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yrkky.mobilecomp.R
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.google.accompanist.insets.systemBarsPadding

@Composable
fun Profile(
    navigationController: NavController,
) {
    val username = remember { mutableStateOf("") }
    val oldPassword = remember { mutableStateOf("") }
    val newPassword = remember { mutableStateOf("") }
    val confirmPassword= remember { mutableStateOf("") }

    fun buttonEnabled(): Boolean {
        return newPassword.value.length >= 8 &&
                oldPassword.value == "salasana123" &&
                newPassword.value == confirmPassword.value
    }

    Surface {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
        ) {
            TopAppBar (
                modifier= Modifier.fillMaxWidth(),
            ) {
                IconButton(
                    onClick = { navigationController.navigate("home") }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = null
                    )
                }

                Text(stringResource(R.string.profile))

                Spacer(modifier = Modifier.fillMaxWidth(1.1f))

            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(16.dp)

            ) {

                Icon(
                    painter = rememberVectorPainter(Icons.Outlined.AccountCircle),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(150.dp),
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = "yrkky",
                    onValueChange = {},
                    label = { Text(stringResource(R.string.username))},
                    shape = RoundedCornerShape(corner = CornerSize(20.dp)),
                    readOnly = true,

                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = oldPassword.value,
                    onValueChange = { passwordString -> oldPassword.value = passwordString },
                    label = { Text(stringResource(R.string.oldpassword))},
                    shape = RoundedCornerShape(corner = CornerSize(20.dp)),
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = newPassword.value,
                    onValueChange = { passwordString -> newPassword.value = passwordString },
                    label = {Text(stringResource(R.string.password))},
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(corner = CornerSize(20.dp))
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = confirmPassword.value,
                    onValueChange = { passwordString -> confirmPassword.value = passwordString },
                    label = {Text(stringResource(R.string.confirmpassword))},
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(corner = CornerSize(20.dp))
                )

                Spacer(modifier = Modifier.height(25.dp))

                Button(
                    onClick = { navigationController.navigate("home") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(corner = CornerSize(20.dp)),
                    enabled = buttonEnabled()
                )
                {
                    Text(text = stringResource(R.string.save))
                }

                Spacer(modifier = Modifier.height(25.dp))

                Button(
                    onClick = { navigationController.navigate("landing") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(corner = CornerSize(20.dp))
                )
                {
                    Text(text = stringResource(R.string.logout))
                }

            }
        }
    }
}