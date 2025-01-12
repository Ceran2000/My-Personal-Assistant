package com.example.mypersonalassistant.ui.create_update_task_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.mypersonalassistant.data.remote.model.ReminderTime
import com.example.mypersonalassistant.data.remote.model.Task
import com.example.mypersonalassistant.ui.component.DatePickerDialogWithThirdButton
import com.example.mypersonalassistant.ui.component.contentDescription
import com.example.mypersonalassistant.util.toMillis
import com.example.mypersonalassistant.util.toStartOfDay
import java.time.*

@Composable
fun MoreActionsButtonMenu(
    modifier: Modifier,
    task: Task,
    index: Int,
    onRemoveTaskClicked: (index: Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var showTimePickerDialog by remember { mutableStateOf(false) }
    var showAddReminderTimeDialog by remember { mutableStateOf(false) }

    Box(modifier = modifier.wrapContentSize(Alignment.TopStart)) {
        IconButton(
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
            DropdownMenuItem(
                text = { Text(text = "Przypomnienie") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AccessAlarm,
                        contentDescription = contentDescription
                    )
                },
                onClick = {
                    showAddReminderTimeDialog = true
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

    if (showAddReminderTimeDialog) {
        AddReminderTimeDialog(
            onConfirmed = { task.addReminder(it) }
        ) { showAddReminderTimeDialog = false }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
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
fun TimePickerDialog(
    onConfirmClick: (hour: Int, minut: Int) -> Unit,
    onDismissRequest: () -> Unit
) {
    val state = rememberTimePickerState()

    com.example.mypersonalassistant.ui.component.TimePickerDialog(
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

@Composable
fun AddReminderTimeDialog(
    onConfirmed: (ReminderTime) -> Unit,
    dismiss: () -> Unit
) {
    var typedTimeAmount by remember { mutableStateOf("1") }
    var selectedTimeUnit by remember { mutableStateOf(ReminderTimeUnit.MINUTES) }

    AlertDialog(
        onDismissRequest = dismiss,
        title = { Text("Przypomnienie") },
        text = {
            Column{
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = typedTimeAmount,
                    onValueChange = { value ->
                        typedTimeAmount = value
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done)
                )
                RadioButtonRow(
                    timeUnit = ReminderTimeUnit.MINUTES,
                    selectedTimeUnit = selectedTimeUnit,
                    onSelected = { selectedTimeUnit = it }
                )
                RadioButtonRow(
                    timeUnit = ReminderTimeUnit.HOUR,
                    selectedTimeUnit = selectedTimeUnit,
                    onSelected = { selectedTimeUnit = it }
                )
                RadioButtonRow(
                    timeUnit = ReminderTimeUnit.DAY,
                    selectedTimeUnit = selectedTimeUnit,
                    onSelected = { selectedTimeUnit = it }
                )
                RadioButtonRow(
                    timeUnit = ReminderTimeUnit.WEEK,
                    selectedTimeUnit = selectedTimeUnit,
                    onSelected = { selectedTimeUnit = it }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmed(ReminderTime(typedTimeAmount.toInt(), selectedTimeUnit))
                    dismiss()
                },
                enabled = typedTimeAmount.isNotBlank()
            ) {
                Text("Zapisz")
            }
        },
        dismissButton = {
            TextButton(onClick = dismiss) { Text("Anuluj") }
        }
    )
}

@Composable
private fun RadioButtonRow(
    timeUnit: ReminderTimeUnit,
    selectedTimeUnit: ReminderTimeUnit,
    onSelected: (ReminderTimeUnit) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .clickable { onSelected(timeUnit) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selectedTimeUnit == timeUnit, onClick = { onSelected(timeUnit) })
        Text(timeUnit.labelText)
    }
}