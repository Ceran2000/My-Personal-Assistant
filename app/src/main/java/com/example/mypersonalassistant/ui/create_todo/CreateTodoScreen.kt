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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mypersonalassistant.ui.component.DefaultAppTopBar
import com.example.mypersonalassistant.ui.component.TopBarTextButton
import com.example.mypersonalassistant.ui.component.contentDescription
import com.example.mypersonalassistant.ui.theme.MyPersonalAssistantTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun CreateTodoScreen(
    viewModel: CreateTodoViewModel = hiltViewModel(),
    navController: NavController
) {
    val saveButtonEnabled by viewModel.saveButtonEnabled.collectAsStateWithLifecycle()
    val showProgressBar by viewModel.showProgressBar

    LaunchedEffect(Unit) {
        viewModel.closeScreen.onEach {
            navController.popBackStack()
        }.launchIn(this)
    }

    MyPersonalAssistantTheme {
        Scaffold(
            topBar = {
                DefaultAppTopBar(
                    title = "Stwórz Listę Zadań",
                    showProgressBar = showProgressBar,
                    navController = navController,
                    actions = {
                        TopBarTextButton(
                            text = "Zapisz",
                            enabled = saveButtonEnabled,
                            onClick = viewModel::onSaveTodoClicked
                        )
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
            CreateTodoScreenContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun CreateTodoScreenContent(
    modifier: Modifier = Modifier,
    viewModel: CreateTodoViewModel
) {
    val todoName by viewModel.todoName.collectAsStateWithLifecycle()
    val tasks by viewModel.todoTasks.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = todoName,
            onValueChange = viewModel::onTodoNameValueChanged,
            label = { Text("Nazwa") }
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
                            .padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .weight(1f, true)
                                .padding(start = 16.dp),
                            value = task.newContentState,
                            onValueChange = { value ->
                                task.newContentState = value
                            },
                            label = { Text("Zadanie") }
                        )
                        IconButton(
                            modifier = Modifier.padding(horizontal = 4.dp),
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