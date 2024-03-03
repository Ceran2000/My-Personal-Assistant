package com.example.mypersonalassistant.ui.note

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val noteNavigationRoute = "note"
const val noteIdArg = "noteId"

fun NavGraphBuilder.noteScreen(navController: NavController) {
    composable(
        route = "$noteNavigationRoute/{$noteIdArg}",
        arguments = listOf(navArgument(noteIdArg) { type = NavType.StringType })
    ) {
        NoteScreen(navController = navController)
    }
}