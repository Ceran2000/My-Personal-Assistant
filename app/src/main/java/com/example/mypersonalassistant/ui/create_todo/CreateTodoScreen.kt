package com.example.mypersonalassistant.ui.create_todo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import com.example.mypersonalassistant.ui.component.TopBarTextButton
import com.example.mypersonalassistant.ui.component.contentDescription
import com.example.mypersonalassistant.ui.theme.MyPersonalAssistantTheme
import com.example.mypersonalassistant.ui.todos.CreateTodoViewModel

@Composable
fun CreateTodoScreen(
    viewModel: CreateTodoViewModel = hiltViewModel(),
    navController: NavController
) {
    val todoName by viewModel.todoName.collectAsStateWithLifecycle()
    val tasks by viewModel.todoTasks.collectAsStateWithLifecycle()

    MyPersonalAssistantTheme {
        Scaffold(
            topBar = {
                DefaultAppTopBar(
                    title = "Stwórz Zadanie",
                    navController = navController,
                    actions = {
                        TopBarTextButton(text = "Zapisz", onClick = viewModel::onSaveTodoClicked)
                    }
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(onClick = viewModel::onAddTaskClicked) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = contentDescription)
                    Text("Dodaj")
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    value = todoName,
                    onValueChange = viewModel::onTodoNameValueChanged
                )

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(
                        items = tasks,
                        key = { index, _ -> index }
                    ) { index, task ->
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                OutlinedTextField(
                                    modifier = Modifier.weight(1f, true),
                                    value = task.newContentState,
                                    onValueChange = { value ->
                                        task.newContentState = value
                                    }
                                )
                                IconButton(
                                    modifier = Modifier.padding(start = 16.dp),
                                    onClick = { viewModel.onDeleteTaskClicked(index) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = contentDescription
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}