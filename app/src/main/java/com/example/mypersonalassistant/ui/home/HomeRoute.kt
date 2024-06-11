package com.example.mypersonalassistant.ui.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object Home

fun NavGraphBuilder.homeScreen(
    navController: NavController
) {
    composable<Home> {
        HomeScreen(navController = navController)
    }
}