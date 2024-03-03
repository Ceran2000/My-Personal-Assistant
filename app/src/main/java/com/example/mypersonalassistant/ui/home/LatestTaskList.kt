package com.example.mypersonalassistant.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.mypersonalassistant.model.TaskList
import com.example.mypersonalassistant.ui.util.UiState

@Composable
fun LatestTaskList(taskList: TaskList) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = taskList.title,
                style = MaterialTheme.typography.titleSmall
            )
            taskList.tasks.take(3).forEachIndexed { index, task ->
                Text(
                    text = "${index + 1}. ${task.content}",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (taskList.tasks.size > 3) {
                Text(
                    text = "I ${taskList.homeScreenMoreTaskCount} więcej..."
                )
            }
        }
    }
}

@Composable
fun LatestTaskListEmptyState() {
    HomeItemEmptyState(message = "Nie masz żadnej listy zadań")
}

@Composable
fun UiState<TaskList>.Component() = when (this) {
    is UiState.Loading -> HomeItemLoadingState()
    is UiState.Empty -> LatestTaskListEmptyState()
    is UiState.ShowContent -> LatestTaskList(taskList = data)
}