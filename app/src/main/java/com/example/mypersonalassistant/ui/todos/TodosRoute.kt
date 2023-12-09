package com.example.mypersonalassistant.ui.todos

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val todosListNavigationRoute = "todos_list"

fun NavGraphBuilder.todosList(navController: NavController) {
    composable(route = todosListNavigationRoute) {
        TodosListScreen(navController = navController)
    }
}