package com.example.mypersonalassistant.ui.create_task_list

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val createTaskListNavigationRoute = "create_task_list"

fun NavGraphBuilder.createTaskList(navController: NavController) {
    composable(route = createTaskListNavigationRoute) {
        CreateTaskListScreen(navController = navController)
    }
}