package com.example.mypersonalassistant.ui.note

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data class Note(val id: String)

fun NavGraphBuilder.noteScreen(navController: NavController) {
    composable<Note> {
        NoteScreen(navController = navController)
    }
}