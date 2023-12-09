package com.example.mypersonalassistant.ui.todos

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mypersonalassistant.ui.component.DefaultAppTopBar
import com.example.mypersonalassistant.ui.component.contentDescription
import com.example.mypersonalassistant.ui.create_todo.createTodoNavigationRoute
import com.example.mypersonalassistant.ui.theme.MyPersonalAssistantTheme

@Composable
fun TodosListScreen(
    viewModel: CreateTodoViewModel = hiltViewModel(),
    navController: NavController
) {
    MyPersonalAssistantTheme {
        val onCreateTaskClicked: () -> Unit = {
            navController.navigate(createTodoNavigationRoute)
        }

        Scaffold(
            topBar = { DefaultAppTopBar(title = "Zadania", navController = navController) },
            floatingActionButton = {
                FloatingActionButton(onClick = onCreateTaskClicked) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = contentDescription
                    )
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding)
            ) {

            }
        }
    }
}