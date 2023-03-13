package com.yrkky.mobilecomp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import com.yrkky.mobilecomp.ui.login.LoginScreen
import androidx.compose.material.Surface
import com.google.android.gms.location.LocationRequest
import com.yrkky.mobilecomp.navigation.MainNavigation
import com.yrkky.mobilecomp.MobileComputingApp
import com.yrkky.mobilecomp.ui.theme.MobileComputingTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 3000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        setContent {
            MobileComputingTheme (darkTheme = true) {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    MainNavigation()
                }
            }
        }
    }
}
