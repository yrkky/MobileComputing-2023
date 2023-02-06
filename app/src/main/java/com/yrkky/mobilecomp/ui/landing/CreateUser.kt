package com.yrkky.mobilecomp.ui.landing

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yrkky.mobilecomp.R
import com.yrkky.mobilecomp.data.entity.UserData


@Composable
fun CreateUserScreen(
    modifier: Modifier,
    navigationController: NavController
) {

    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember {mutableStateOf("")}

    fun buttonEnabled(): Boolean {
        return password.value.length >= 8 && confirmPassword.value == password.value
    }

    Column (
        modifier = modifier.padding(20.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
    ) {

        Icon(
            painter = rememberVectorPainter(image = Icons.Outlined.AccountCircle),
            modifier = Modifier
                .fillMaxWidth()
                .size(150.dp),
            contentDescription = "login_image"
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = username.value,
            onValueChange = { usernameString -> username.value = usernameString },
            label = { Text(stringResource(R.string.username)) },
            shape = RoundedCornerShape(corner = CornerSize(10.dp))
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password.value,
            onValueChange = { passwordString -> password.value = passwordString },
            label = { Text(stringResource(R.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(corner = CornerSize(10.dp))
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = confirmPassword.value,
            onValueChange = { confirmPasswordString -> confirmPassword.value = confirmPasswordString },
            label = { Text(stringResource(R.string.confirmpassword)) },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(corner = CornerSize(10.dp))
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (UserData().username == username.value && UserData().password == password.value) {
                    navigationController.navigate("home")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
            ,
            shape = RoundedCornerShape(corner = CornerSize(10.dp)),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(32,57,120,255)),
            enabled = buttonEnabled()
        ) {
            Text(stringResource(R.string.createaccount))

        }

    }
}
