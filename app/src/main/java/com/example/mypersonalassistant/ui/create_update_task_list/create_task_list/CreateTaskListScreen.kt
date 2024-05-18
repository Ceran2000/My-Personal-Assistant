package com.example.mypersonalassistant.ui.create_update_task_list.create_task_list

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mypersonalassistant.R
import com.example.mypersonalassistant.ui.component.DefaultAppTopBar
import com.example.mypersonalassistant.ui.component.TopBarTextButton
import com.example.mypersonalassistant.ui.component.contentDescription
import com.example.mypersonalassistant.ui.create_update_task_list.TaskItem
import com.example.mypersonalassistant.ui.theme.AppTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun CreateTaskListScreen(
    viewModel: CreateTaskListViewModel = hiltViewModel(),
    navController: NavController
) {
    val saveButtonEnabled by viewModel.saveButtonEnabled.collectAsStateWithLifecycle()
    val showProgressBar by viewModel.showProgressBar.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.closeScreen.onEach {
            navController.popBackStack()
        }.launchIn(this)
    }

    AppTheme {
        Scaffold(
            topBar = {
                DefaultAppTopBar(
                    title = stringResource(R.string.create_task_list_appbar_title),
                    showProgressBar = showProgressBar,
                    navController = navController,
                    actions = {
                        TopBarTextButton(
                            text = stringResource(R.string.create_task_list_save_button),
                            enabled = saveButtonEnabled,
                            onClick = viewModel::onSaveListClicked
                        )
                    }
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(onClick = viewModel::onAddTaskClicked) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = contentDescription)
                    Text(stringResource(R.string.create_task_list_add_button))
                }
            }
        ) { padding ->
            CreateTaskListScreenContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun CreateTaskListScreenContent(
    modifier: Modifier = Modifier,
    viewModel: CreateTaskListViewModel
) {
    val title by viewModel.listName.collectAsStateWithLifecycle()
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = title,
            onValueChange = viewModel::onListNameValueChanged,
            label = { Text(stringResource(R.string.create_task_list_name_label)) }
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
                TaskItem(
                    task = task,
                    index = index,
                    onContentValueChanged = viewModel::onTaskContentValueChanged,
                    onRemoveTaskClicked = viewModel::onDeleteTaskClicked
                )
            }
        }
    }
}