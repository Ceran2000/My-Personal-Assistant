package com.example.mypersonalassistant.ui.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val homeNavigationRoute = "home"

fun NavGraphBuilder.homeScreen(
    navController: NavController
) {
    composable(route = homeNavigationRoute) {
        HomeScreen(navController = navController)
    }
}