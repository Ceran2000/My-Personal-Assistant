package com.example.mypersonalassistant.ui.task_lists

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val taskListsNavigationRoute = "task_lists"

fun NavGraphBuilder.taskLists(navController: NavController) {
    composable(route = taskListsNavigationRoute) {
        TaskLists(navController = navController)
    }
}