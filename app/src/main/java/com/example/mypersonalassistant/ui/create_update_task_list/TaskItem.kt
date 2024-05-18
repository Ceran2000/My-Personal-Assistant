package com.example.mypersonalassistant.ui.create_update_task_list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mypersonalassistant.data.remote.model.Task
import com.example.mypersonalassistant.ui.component.DatePickerDialogWithThirdButton
import com.example.mypersonalassistant.ui.component.TimePickerDialog
import com.example.mypersonalassistant.ui.component.contentDescription
import com.example.mypersonalassistant.util.toMillis
import com.example.mypersonalassistant.util.toStartOfDay
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

@Composable
fun TaskItem(
    task: Task,
    index: Int,
    onContentValueChanged: () -> Unit,
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
                    .size(36.dp),
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
            Column(
                modifier = Modifier
                    .weight(1f, true)
                    .padding(start = 16.dp)
            ) {
                BasicTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = task.newContent,
                    onValueChange = {
                        task.newContent = it
                        onContentValueChanged()
                    },
                    textStyle = MaterialTheme.typography.bodyMedium
                )
                if (task.hasEndDateTimeSet) {
                    Row(
                        modifier = Modifier.padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = contentDescription
                        )
                        Text(
                            modifier = Modifier.padding(start = 4.dp),
                            text = task.endDateTimeString!!,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            MoreActionsButtonMenu(
                task = task,
                index = index,
                onRemoveTaskClicked = onRemoveTaskClicked
            )

        }
    }
}

@Composable
private fun MoreActionsButtonMenu(
    task: Task,
    index: Int,
    onRemoveTaskClicked: (index: Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var showTimePickerDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
        IconButton(
            modifier = Modifier.padding(horizontal = 4.dp),
            onClick = { expanded = true }
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = contentDescription
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Usuń") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = contentDescription
                    )
                },
                onClick = {
                    onRemoveTaskClicked(index)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text(text = "Data") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = contentDescription
                    )
                },
                onClick = {
                    showDatePickerDialog = true
                    expanded = false
                }
            )
        }
    }

    if (showDatePickerDialog) {
        DatePickerDialog(
            initialDateMillis = task.newEndDateUTCMillis,
            minPickerDateMillis = ZonedDateTime.now().toStartOfDay().toMillis(),
            onConfirmClick = { selectedDateUTCMillis ->
                task.newEndDateUTCMillis = selectedDateUTCMillis
                showTimePickerDialog = true
            },
            onClearClick = { task.newEndDateUTCMillis = null },
            onDismissRequest = { showDatePickerDialog = false }
        )
    }

    if (showTimePickerDialog) {
        TimePickerDialog(
            onConfirmClick = { hour, minute ->
                val instant = Instant.ofEpochMilli(task.newEndDateUTCMillis!!)
                val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).withHour(hour).withMinute(minute)
                val zonedDateTime = localDateTime.atZone(ZoneId.of("UTC"))
                task.newEndDateUTCMillis = zonedDateTime.toMillis()
            },
            onDismissRequest = { showTimePickerDialog = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerDialog(
    initialDateMillis: Long?,
    minPickerDateMillis: Long,
    onConfirmClick: (selectedDateMillis: Long) -> Unit,
    onClearClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val selectableDates: SelectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean =
            utcTimeMillis >= minPickerDateMillis
    }
    val state = rememberDatePickerState(
        initialDisplayedMonthMillis = initialDateMillis,
        selectableDates = selectableDates
    )
    val confirmButtonEnabled by remember { derivedStateOf { state.selectedDateMillis != null } }

    DatePickerDialogWithThirdButton(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                enabled = confirmButtonEnabled,
                onClick = {
                    onConfirmClick(state.selectedDateMillis!!)
                    onDismissRequest()
                }
            ) {
                Text(text = stringResource(android.R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(android.R.string.cancel))
            }
        },
        thirdButton = {
            TextButton(onClick = {
                onClearClick()
                onDismissRequest()
            }) {
                Text(text = "Wyczyść")
            }
        }
    ) {
        DatePicker(state = state)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerDialog(
    onConfirmClick: (hour: Int, minut: Int) -> Unit,
    onDismissRequest: () -> Unit
) {
    val state = rememberTimePickerState()

    TimePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = {
                onConfirmClick(state.hour, state.minute)
                onDismissRequest()
            }) {
                Text(text = stringResource(id = android.R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(android.R.string.cancel))
            }
        }
    ) {
        TimePicker(state = state)
    }
}