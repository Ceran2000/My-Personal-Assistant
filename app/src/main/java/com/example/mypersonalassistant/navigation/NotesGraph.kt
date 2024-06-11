package com.example.mypersonalassistant.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.example.mypersonalassistant.ui.note.noteScreen
import com.example.mypersonalassistant.ui.notes.Notes
import com.example.mypersonalassistant.ui.notes.notesScreen
import kotlinx.serialization.Serializable

@Serializable
object NotesNav

fun NavGraphBuilder.notesGraph(navController: NavController) {
    navigation<NotesNav>(startDestination = Notes) {
        notesScreen(navController = navController)
        noteScreen(navController = navController)
    }
}