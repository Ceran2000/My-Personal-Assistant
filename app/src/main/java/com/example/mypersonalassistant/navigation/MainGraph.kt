package com.example.mypersonalassistant.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.example.mypersonalassistant.ui.home.Home
import com.example.mypersonalassistant.ui.home.homeScreen
import kotlinx.serialization.Serializable

@Serializable
object Main

fun NavGraphBuilder.mainGraph(navController: NavController) {
    navigation<Main>(startDestination = Home) {
        homeScreen(navController)
        notesGraph(navController)
        taskListsGraph(navController)
    }
}