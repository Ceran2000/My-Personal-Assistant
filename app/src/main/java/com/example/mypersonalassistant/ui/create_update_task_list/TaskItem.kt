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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mypersonalassistant.ui.component.DatePickerDialogWithThirdButton
import com.example.mypersonalassistant.ui.component.contentDescription
import com.example.mypersonalassistant.ui.create_task_list.Task
import java.time.ZonedDateTime

@Composable
fun TaskItem(
    task: Task,
    index: Int,
    onContentValueChanged: () -> Unit,
    onRemoveTaskClicked: (index: Int) -> Unit
) {
    val context = LocalContext.current

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
                            text = task.endDateTimeString(context)!!,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MoreActionsButtonMenu(
    task: Task,
    index: Int,
    onRemoveTaskClicked: (index: Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showDatePickerDialog by remember { mutableStateOf(false) }

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
        val state = rememberDatePickerState(initialDisplayedMonthMillis = task.newEndDateMillis)
        val confirmButtonEnabled by remember { derivedStateOf { state.selectedDateMillis != null } }
        val onDismissRequest = { showDatePickerDialog = false }
        val minPickerTimeMillis: Long = ZonedDateTime.now().toInstant().toEpochMilli()      //TODO: ADD TIME!!

        DatePickerDialogWithThirdButton(
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(
                    enabled = confirmButtonEnabled,
                    onClick = {
                        task.newEndDateMillis = state.selectedDateMillis
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
                    task.newEndDateMillis = null
                    onDismissRequest()
                }) {
                    Text(text = "Wyczyść")
                }
            }
        ) {
            DatePicker(
                state = state,
                dateValidator = {
                    it >= minPickerTimeMillis
                }
            )
        }
    }
}