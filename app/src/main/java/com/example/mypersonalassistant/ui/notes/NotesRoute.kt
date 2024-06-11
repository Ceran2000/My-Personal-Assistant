package com.example.mypersonalassistant.ui.notes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object Notes

fun NavGraphBuilder.notesScreen(navController: NavController) {
    composable<Notes> {
        NotesScreen(navController = navController)
    }
}