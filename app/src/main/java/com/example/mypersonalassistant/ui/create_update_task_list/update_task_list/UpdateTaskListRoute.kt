package com.example.mypersonalassistant.ui.create_update_task_list.update_task_list

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kotlinx.serialization.Serializable

@Serializable
data class UpdateTaskList(val id: String)

fun NavGraphBuilder.updateTaskList(navController: NavController) {
    composable<UpdateTaskList> {
        UpdateTaskListScreen(navController = navController)
    }
}