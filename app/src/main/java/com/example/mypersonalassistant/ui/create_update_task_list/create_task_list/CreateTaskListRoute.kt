package com.example.mypersonalassistant.ui.create_update_task_list.create_task_list

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object CreateTaskList

fun NavGraphBuilder.createTaskList(navController: NavController) {
    composable<CreateTaskList> {
        CreateTaskListScreen(navController = navController)
    }
}