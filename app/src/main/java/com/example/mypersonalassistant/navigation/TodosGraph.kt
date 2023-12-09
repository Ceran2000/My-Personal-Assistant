package com.example.mypersonalassistant.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.example.mypersonalassistant.ui.create_todo.createTodo
import com.example.mypersonalassistant.ui.todos.todosList
import com.example.mypersonalassistant.ui.todos.todosListNavigationRoute

const val todosNavigationRoute = "todos"

fun NavGraphBuilder.todosGraph(navController: NavController) {
    navigation(startDestination = todosListNavigationRoute, route = todosNavigationRoute) {
        todosList(navController = navController)
        createTodo(navController = navController)
    }
}