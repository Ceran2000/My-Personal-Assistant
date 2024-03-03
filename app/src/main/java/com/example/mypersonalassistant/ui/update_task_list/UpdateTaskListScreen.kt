package com.example.mypersonalassistant.ui.update_task_list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mypersonalassistant.R
import com.example.mypersonalassistant.ui.component.DefaultAppTopBar
import com.example.mypersonalassistant.ui.component.TopBarTextButton
import com.example.mypersonalassistant.ui.component.contentDescription
import com.example.mypersonalassistant.ui.create_task_list.Task
import com.example.mypersonalassistant.ui.theme.AppTheme

@Composable
fun UpdateTaskListScreen(
    viewModel: UpdateTaskListViewModel = hiltViewModel(),
    navController: NavController
) {
    AppTheme {
        Scaffold(
            topBar = {
                DefaultAppTopBar(
                    title = stringResource(R.string.update_task_list_appbar_title),
                    navController = navController,
                    actions = {
                        TopBarTextButton(
                            text = stringResource(R.string.update_task_list_appbar_button_save),
                            enabled = true,     //TODO
                            onClick = viewModel::onSaveButtonClicked
                        )
                    }
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(onClick = viewModel::onAddTaskClicked) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = contentDescription
                    )
                    Text(stringResource(R.string.update_task_list_fab_add_button))
                }
            }
        ) { padding ->
            TaskListScreenContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun TaskListScreenContent(
    modifier: Modifier = Modifier,
    viewModel: UpdateTaskListViewModel
) {
    val title by viewModel.listTitle.collectAsStateWithLifecycle()
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = title,
            onValueChange = viewModel::onTitleValueChanged,
            label = { Text(stringResource(R.string.update_task_list_task_title_label)) }
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(
                items = tasks,
                key = { index, _ -> index }
            ) { index, item ->
                TaskItem(
                    task = item,
                    index = index,
                    onRemoveTaskClicked = viewModel::onRemoveTaskClicked
                )
            }
        }
    }
}

@Composable
private fun TaskItem(
    task: Task,
    index: Int,
    onRemoveTaskClicked: (index: Int) -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedCard(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(48.dp),
                shape = CircleShape,
                colors = CardDefaults.outlinedCardColors(Color.Transparent),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("${index + 1}")
                }
            }
            BasicTextField(
                modifier = Modifier
                    .weight(1f, true)
                    .padding(start = 16.dp),
                value = task.newContent,
                onValueChange = {
                    task.newContent = it
                }
            )
            IconButton(
                modifier = Modifier.padding(horizontal = 4.dp),
                onClick = { onRemoveTaskClicked(index) }
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = contentDescription
                )
            }
        }
    }
}