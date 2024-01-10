package com.example.mypersonalassistant.ui.todos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mypersonalassistant.ui.component.DefaultAppTopBar
import com.example.mypersonalassistant.ui.component.contentDescription
import com.example.mypersonalassistant.ui.create_todo.createTodoNavigationRoute
import com.example.mypersonalassistant.ui.theme.MyPersonalAssistantTheme

@Composable
fun TodosListScreen(
    viewModel: TodosViewModel = hiltViewModel(),
    navController: NavController
) {
    MyPersonalAssistantTheme {
        val todos by viewModel.todos.collectAsStateWithLifecycle()
        val onCreateTaskClicked: () -> Unit = { navController.navigate(createTodoNavigationRoute) }


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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(todos, { it.id }) {
                    it.Item(modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}