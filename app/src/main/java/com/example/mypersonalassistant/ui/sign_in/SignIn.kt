package com.example.mypersonalassistant.ui.sign_in

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object SignIn

fun NavGraphBuilder.signInScreen(navController: NavController) {
    composable<SignIn> {
        SignInScreen(navController)
    }
}