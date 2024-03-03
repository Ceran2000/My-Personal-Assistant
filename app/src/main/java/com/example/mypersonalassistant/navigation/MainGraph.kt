package com.example.mypersonalassistant.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.example.mypersonalassistant.ui.home.homeNavigationRoute
import com.example.mypersonalassistant.ui.home.homeScreen
import com.example.mypersonalassistant.ui.notes.notesScreen

const val mainNavigationRoute = "main"

fun NavGraphBuilder.mainGraph(navController: NavController) {
    navigation(startDestination = homeNavigationRoute, route = mainNavigationRoute) {
        homeScreen(navController)
        notesGraph(navController)
        taskListsGraph(navController)
    }
}