package com.yrkky.mobilecomp.ui.landing

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yrkky.mobilecomp.R
import com.yrkky.mobilecomp.data.entity.UserData


@Composable
fun LandingScreen(
    modifier: Modifier,
    navigationController: NavController
) {

    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    fun buttonEnabled(): Boolean {
        return password.value.length >= 8
    }

    Column (
        modifier = modifier.padding(20.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
    ) {
        
        Text(
            text = "Welcome to reminder app",

        )

        Spacer(modifier = Modifier.height(150.dp))


        Button(
            onClick = {
                navigationController.navigate("createuser")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
            ,
            shape = RoundedCornerShape(corner = CornerSize(10.dp)),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(32,57,120,255)),
        ) {
            Text(stringResource(R.string.signup))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                navigationController.navigate("login")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
            ,
            shape = RoundedCornerShape(corner = CornerSize(10.dp)),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(32,57,120,255)),
        ) {
            Text(stringResource(R.string.login))
        }

    }
}
