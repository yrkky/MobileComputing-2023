package com.yrkky.mobilecomp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yrkky.mobilecomp.MobileComputingAppState
import com.yrkky.mobilecomp.rememberMobileComputingAppState
import com.yrkky.mobilecomp.ui.home.Home
import com.yrkky.mobilecomp.ui.landing.CreateUserScreen
import com.yrkky.mobilecomp.ui.landing.LandingScreen
import com.yrkky.mobilecomp.ui.login.LoginScreen
import com.yrkky.mobilecomp.ui.reminder.Reminder
import com.yrkky.mobilecomp.ui.userProfile.Profile

@Composable
fun MobileComputingApp (
    appState: MobileComputingAppState = rememberMobileComputingAppState()
) {

    NavHost(
        navController = appState.navigationController,
        startDestination = "landing"
    ) {
        composable(route = "landing") {
            LandingScreen(navigationController = appState.navigationController, modifier = Modifier.fillMaxSize())
        }
        composable(route = "login") {
            LoginScreen(navigationController = appState.navigationController, modifier = Modifier.fillMaxSize())
        }
        composable(route = "createuser") {
            CreateUserScreen(navigationController = appState.navigationController, modifier = Modifier.fillMaxSize())
        }
        composable(route = "home") {
            Home(navigationController = appState.navigationController, modifier = Modifier.fillMaxSize())
        }
        composable(route = "reminder") {
            Reminder(navigationController = appState.navigationController, onBackPress = appState::navigateBack )
        }
        composable(route = "profile") {
            Profile(navigationController = appState.navigationController, onBackPress = appState::navigateBack )
        }
    }

}