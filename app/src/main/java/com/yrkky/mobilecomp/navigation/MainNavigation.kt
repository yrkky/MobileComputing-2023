package com.yrkky.mobilecomp.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yrkky.mobilecomp.ui.home.Home
import com.yrkky.mobilecomp.ui.landing.CreateUserScreen
import com.yrkky.mobilecomp.ui.landing.LandingScreen
import com.yrkky.mobilecomp.ui.login.LoginScreen
import com.yrkky.mobilecomp.ui.reminder.EditReminder
import com.yrkky.mobilecomp.ui.reminder.Reminder
import com.yrkky.mobilecomp.ui.userProfile.Profile

@Composable
fun MainNavigation() {
    val navigationController = rememberNavController()

    NavHost(
        navController = navigationController,
        startDestination = "landing"
    ) {
        composable("landing") {
            LandingScreen(navigationController = navigationController, modifier = Modifier.fillMaxSize())
        }
        composable(route = "login") {
            LoginScreen(navigationController = navigationController, modifier = Modifier.fillMaxSize())
        }
        composable(route = "createuser") {
            CreateUserScreen(navigationController = navigationController, modifier = Modifier.fillMaxSize())
        }
        composable(route = "home") {
            Home(navigationController = navigationController, modifier = Modifier.fillMaxSize())
        }
        composable(route = "reminder") {
            Reminder(navigationController = navigationController)
        }
        composable(route = "profile") {
            Profile(navigationController = navigationController)
        }
        composable(route = "editreminder") {
                EditReminder(navigationController = navigationController)
        }
    }
}