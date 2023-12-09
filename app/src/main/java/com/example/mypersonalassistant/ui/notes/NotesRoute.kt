package com.example.mypersonalassistant.ui.notes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val notesNavigationRoute = "notes"

fun NavGraphBuilder.notesScreen(navController: NavController) {
    composable(route = notesNavigationRoute) {
        NotesScreen(navController = navController)
    }
}