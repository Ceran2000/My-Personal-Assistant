package com.example.mypersonalassistant.ui.sign_in

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val signInNavigationRoute = "sign_in"

fun NavGraphBuilder.signInScreen(navController: NavController) {
    composable(route = signInNavigationRoute) {
        SignInScreen(navController)
    }
}