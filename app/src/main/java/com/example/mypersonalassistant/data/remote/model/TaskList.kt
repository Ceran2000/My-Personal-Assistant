package com.example.mypersonalassistant.data.remote.model

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.mypersonalassistant.ui.component.contentDescription
import com.example.mypersonalassistant.ui.create_update_task_list.ReminderTimeUnit
import com.google.firebase.firestore.DocumentSnapshot

data class TaskList(val id: String, val title: String, val tasks: List<Task>) {

    val homeScreenMoreTaskCount: Int by lazy {
        if (tasks.size > 3) tasks.size - 3 else 0
    }

    private var expanded by mutableStateOf(false)

    @Composable
    fun Item(
        modifier: Modifier = Modifier,
        onClick: (id: String) -> Unit,
    ) {
        val dropDownIconRotation: Float by animateFloatAsState(targetValue = if (!expanded) 0f else 180f, label = "dropDown")

        ElevatedCard(
            onClick = { onClick(id) },
            modifier = modifier,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1.0f),
                    text = title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    onClick = { expanded = !expanded }
                ) {
                    Icon(
                        modifier = Modifier.rotate(dropDownIconRotation),
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = contentDescription
                    )
                }
            }
            if (expanded) {
                tasks.forEachIndexed { index, task ->
                    Column(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = "${index+1}. ${task.newContent}",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

fun DocumentSnapshot.toTaskList(): TaskList {

    val taskList = tasks.map { it.toTask() }

    return TaskList(
        id = id,
        title = getString("title").orEmpty(),
        tasks = taskList
    )
}

@Suppress("UNCHECKED_CAST")
private val DocumentSnapshot.tasks: List<HashMap<String, Any>> get() = get("tasks") as? List<HashMap<String, Any>> ?: emptyList()

@Suppress("UNCHECKED_CAST")
private fun Map<String, Any>.toTask(): Task {
    val content = get("content") as String
    val endDateUTCMillis = get("endTimeMillis") as? Long
    val reminderTimes = (get("reminderTimes") as? List<Map<String, Any>>)
        ?.map { it.toReminderTime() }
        ?.toSet()
        ?: emptySet()

    return Task(content, endDateUTCMillis, reminderTimes)
}

private fun Map<String, Any>.toReminderTime(): ReminderTime {
    val timeAmount = (get("timeAmount") as Long).toInt()
    val unit = (get("unit") as String).let { ReminderTimeUnit.valueOf(it) }
    return ReminderTime(timeAmount, unit)
}