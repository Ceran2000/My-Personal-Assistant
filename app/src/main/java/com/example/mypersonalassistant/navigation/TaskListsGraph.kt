package com.example.mypersonalassistant.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.example.mypersonalassistant.ui.create_update_task_list.create_task_list.createTaskList
import com.example.mypersonalassistant.ui.task_lists.taskLists
import com.example.mypersonalassistant.ui.task_lists.taskListsNavigationRoute
import com.example.mypersonalassistant.ui.create_update_task_list.update_task_list.updateTaskList

const val taskListsGraphNavigationRoute = "taskListsGraph"

fun NavGraphBuilder.taskListsGraph(navController: NavController) {
    navigation(startDestination = taskListsNavigationRoute, route = taskListsGraphNavigationRoute) {
        taskLists(navController = navController)
        createTaskList(navController = navController)
        updateTaskList(navController = navController)
    }
}