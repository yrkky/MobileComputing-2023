package com.yrkky.mobilecomp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class MobileComputingAppState (
    val navigationController: NavHostController
) {
    fun navigateBack() {
        navigationController.popBackStack()
    }


}

@Composable
fun rememberMobileComputingAppState(
    navigationController: NavHostController = rememberNavController()
) = remember(navigationController) {
    MobileComputingAppState(navigationController)
}