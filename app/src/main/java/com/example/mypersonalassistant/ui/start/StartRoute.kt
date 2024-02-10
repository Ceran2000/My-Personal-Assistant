package com.example.mypersonalassistant.ui.start

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val startNavigationRoute = "start"

fun NavGraphBuilder.startScreen(navController: NavController) {
    composable(route = startNavigationRoute) {
        StartScreen()
    }
}