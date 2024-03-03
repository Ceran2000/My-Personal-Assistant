package com.example.mypersonalassistant.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.example.mypersonalassistant.ui.note.noteScreen
import com.example.mypersonalassistant.ui.notes.notesNavigationRoute
import com.example.mypersonalassistant.ui.notes.notesScreen

const val notesGraphNavigationRoute = "notesGraph"

fun NavGraphBuilder.notesGraph(navController: NavController) {
    navigation(startDestination = notesNavigationRoute, route = notesGraphNavigationRoute) {
        notesScreen(navController = navController)
        noteScreen(navController = navController)
    }
}