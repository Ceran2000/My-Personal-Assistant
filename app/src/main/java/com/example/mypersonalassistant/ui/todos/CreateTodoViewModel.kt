package com.example.mypersonalassistant.ui.todos

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.mypersonalassistant.ui.util.EMPTY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CreateTodoViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val _todoName = MutableStateFlow(String.EMPTY)
    val todoName: StateFlow<String> = _todoName.asStateFlow()

    fun onTodoNameValueChanged(value: String) {
        _todoName.value = value
    }

    private val _todoTasks = MutableStateFlow<List<Task>>(emptyList())
    val todoTasks = _todoTasks.asStateFlow()

    fun onAddTaskClicked() {
        _todoTasks.update {
            val list = it.toMutableList()
            list.add(Task.empty())
            list
        }
    }

    fun onDeleteTaskClicked(index: Int) {
        _todoTasks.update {
            val list = it.toMutableList()
            list.removeAt(index)
            list
        }
    }

    fun onSaveTodoClicked() {

    }
}

data class Task(val id: String, val content: String) {

    //var enabled by mutableStateOf(false)
    var newContentState by mutableStateOf(content)
/*    val newContentState by _newContentState

    var newContent: String = content
        set (input) {
            field = input
            _newContentState.value = input
        }*/

    companion object {
        fun empty() = Task(id = UUID.randomUUID().toString(), String.EMPTY)
    }
}