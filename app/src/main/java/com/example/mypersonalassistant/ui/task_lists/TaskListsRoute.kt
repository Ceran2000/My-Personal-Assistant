package com.example.mypersonalassistant.ui.task_lists

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object TaskLists

fun NavGraphBuilder.taskLists(navController: NavController) {
    composable<TaskLists> {
        TaskLists(navController = navController)
    }
}