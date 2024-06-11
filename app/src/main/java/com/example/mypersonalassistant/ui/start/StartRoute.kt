package com.example.mypersonalassistant.ui.start

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object Start

fun NavGraphBuilder.startScreen(navController: NavController) {
    composable<Start> {
        StartScreen()
    }
}