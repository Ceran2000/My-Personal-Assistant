package com.example.mypersonalassistant.ui.create_update_task_list

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.example.mypersonalassistant.data.remote.model.ReminderTime
import com.example.mypersonalassistant.data.remote.model.Task
import com.example.mypersonalassistant.ui.component.contentDescription


@Composable
fun TaskItem(
    task: Task,
    index: Int,
    onContentValueChanged: () -> Unit,
    onRemoveTaskClicked: (index: Int) -> Unit
) {
    val placeholderText = remember { "Zadanie ${index + 1}"}
    val bottomPadding = if (task.hasAnyOptionSet) 12.dp else 4.dp

    val showTextFieldBottomPadding = task.hasAnyReminderTimeSet

    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, bottom = bottomPadding)
                .animateContentSize()
        ) {
            Row(
                modifier = Modifier.padding(top = 4.dp)
            ) {
                TextField(
                    modifier = Modifier
                        .weight(1f, true)
                        .padding(top = 12.dp),
                    placeholderText, task, onContentValueChanged
                )

                MoreActionsButtonMenu(
                    modifier = Modifier.align(Alignment.Top),
                    task = task,
                    index = index,
                    onRemoveTaskClicked = onRemoveTaskClicked
                )
            }

            if (showTextFieldBottomPadding) Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier.padding(end = 12.dp)
            ) {
                if (task.hasEndDateTimeSet) EndDateTime(task)

                task.newReminderTimes.forEach { reminderTime ->
                    ReminderTime(reminderTime, onClear = { task.removeReminder(reminderTime) })
                }
            }
        }
    }
}

@Composable
private fun TextField(
    modifier: Modifier,
    placeholderText: String,
    task: Task,
    onContentValueChanged: () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        BasicTextField(
            modifier = Modifier.fillMaxWidth(),
            value = task.newContent,
            onValueChange = {
                task.newContent = it
                onContentValueChanged()
            },
            textStyle = MaterialTheme.typography.bodyLarge
        )
        if (task.newContent.isEmpty()) {
            Text(
                text = placeholderText,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic
            )
        }
    }
}

@Composable
private fun EndDateTime(
    task: Task
) {
    SettingsRow(
        label = task.endDateTimeString!!,
        icon = Icons.Default.CalendarMonth,
        onClear = task::clearNewEndDate
    )
}

@Composable
private fun ReminderTime(
    reminderTime: ReminderTime,
    onClear: () -> Unit
) {
    SettingsRow(
        label = reminderTime.toString(),
        icon = Icons.Default.Alarm,
        onClear = onClear
    )
}

@Composable
private fun SettingsRow(
    label: String,
    icon: ImageVector,
    onClear: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        )
        ClickableIcon(
            icon = Icons.Default.Clear,
            onClick = onClear
        )
    }
}

@Composable
fun ClickableIcon(
    icon: ImageVector,
    onClick: () -> Unit
) {
    val indication = remember { ripple() }

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .clickable(
                onClick = onClick,
                indication = indication,
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier
                .padding(4.dp)
                .size(18.dp)
        )
    }
}