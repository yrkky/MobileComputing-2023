package com.yrkky.mobilecomp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import com.yrkky.mobilecomp.ui.login.LoginScreen
import androidx.compose.material.Surface
import com.yrkky.mobilecomp.ui.theme.MobileComputingTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileComputingTheme (darkTheme = true) {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    LoginScreen(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}
