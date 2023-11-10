package com.example.mypersonalassistant.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.mypersonalassistant.ui.home.homeScreen
import com.example.mypersonalassistant.ui.sign_in.signInNavigationRoute
import com.example.mypersonalassistant.ui.sign_in.signInScreen

@Composable
fun MpaNavHost(modifier: Modifier = Modifier, navController: NavHostController) {
    NavHost(navController = navController, startDestination = signInNavigationRoute, modifier = modifier) {
        signInScreen(navController)
        homeScreen(navController)
    }
}