package com.example.mypersonalassistant.ui.create_update_task_list.update_task_list

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val updateTaskListNavigationRoute = "update_task_list"
const val taskListIdArg = "taskListId"

fun NavGraphBuilder.updateTaskList(navController: NavController) {
    composable(
        route = "$updateTaskListNavigationRoute/{$taskListIdArg}",
        arguments = listOf(navArgument(taskListIdArg) { type = NavType.StringType })
    ) {
        UpdateTaskListScreen(navController = navController)
    }
}