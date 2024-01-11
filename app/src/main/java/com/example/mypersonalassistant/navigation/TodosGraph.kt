package com.example.mypersonalassistant.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.example.mypersonalassistant.ui.create_task_list.createTaskList
import com.example.mypersonalassistant.ui.task_lists.taskLists
import com.example.mypersonalassistant.ui.task_lists.taskListsNavigationRoute

const val todosNavigationRoute = "todos"

fun NavGraphBuilder.todosGraph(navController: NavController) {
    navigation(startDestination = taskListsNavigationRoute, route = todosNavigationRoute) {
        taskLists(navController = navController)
        createTaskList(navController = navController)
    }
}