package com.example.mypersonalassistant.data.remote.model

import androidx.compose.runtime.*
import com.example.mypersonalassistant.ui.create_update_task_list.ReminderTimeUnit
import com.example.mypersonalassistant.ui.util.EMPTY
import com.example.mypersonalassistant.util.toFormattedString
import com.example.mypersonalassistant.util.toZonedDateTime

data class Task(private val content: String, private val endDateUTCMillis: Long?, private val reminderTimes: Set<ReminderTime>) {

    var newContent by mutableStateOf(content)
    val isNotEmpty get() = newContent.isNotEmpty()

    var newEndDateUTCMillis by mutableStateOf(endDateUTCMillis)

    fun clearNewEndDate() {
        newEndDateUTCMillis = null
    }

    val hasEndDateTimeSet get() = newEndDateUTCMillis != null
    val endDateTimeString get() = newEndDateUTCMillis?.toZonedDateTime()?.toFormattedString()

    private val _newReminderTimes = mutableStateOf(reminderTimes)
    val newReminderTimes by _newReminderTimes

    val hasAnyReminderTimeSet get() = newReminderTimes.isNotEmpty()

    fun addReminder(reminderTime: ReminderTime) {
        val newReminders = newReminderTimes.toMutableSet().apply { add(reminderTime) }
        _newReminderTimes.value = newReminders
    }

    fun removeReminder(reminderTime: ReminderTime) {
        val newReminders = newReminderTimes.toMutableSet().apply { remove(reminderTime) }
        _newReminderTimes.value = newReminders
    }

    val hasAnyOptionSet get() = hasEndDateTimeSet || hasAnyReminderTimeSet

    fun toFirestoreInput() = hashMapOf(
        "content" to newContent,
        "endTimeMillis" to newEndDateUTCMillis,
        "reminderTimes" to newReminderTimes.map { reminder ->
            reminder.toFirestoreInput()
        }
    )

    companion object {
        fun empty() = Task(content = String.EMPTY, endDateUTCMillis = null, reminderTimes = emptySet())
    }
}

data class ReminderTime(val timeAmount: Int, val unit: ReminderTimeUnit) {

    private val minutes = timeAmount * unit.minuteMultipler

    override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is ReminderTime) return false
            return minutes == other.minutes
        }

    override fun hashCode(): Int = minutes.hashCode()

    override fun toString(): String = "$timeAmount ${unit.labelText}"

    fun toFirestoreInput() = mapOf(
        "timeAmount" to timeAmount,
        "unit" to unit.name
    )
}