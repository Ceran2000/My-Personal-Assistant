package com.example.mypersonalassistant.data.remote.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.mypersonalassistant.ui.util.EMPTY
import com.example.mypersonalassistant.util.toFormattedString
import com.example.mypersonalassistant.util.toZonedDateTime

data class Task(private val content: String, private val endDateUTCMillis: Long?) {

    var newContent by mutableStateOf(content)
    val isNotEmpty get() = newContent.isNotEmpty()

    var newEndDateUTCMillis by mutableStateOf(endDateUTCMillis)

    val hasEndDateTimeSet get() = newEndDateUTCMillis != null
    val endDateTimeString get() = newEndDateUTCMillis?.toZonedDateTime()?.toFormattedString()

    companion object {
        fun empty() = Task(String.EMPTY, null)
    }
}