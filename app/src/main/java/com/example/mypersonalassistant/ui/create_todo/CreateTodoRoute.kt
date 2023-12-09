package com.example.mypersonalassistant.ui.create_todo

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val createTodoNavigationRoute = "create_todo"

fun NavGraphBuilder.createTodo(navController: NavController) {
    composable(route = createTodoNavigationRoute) {
        CreateTodoScreen(navController = navController)
    }
}