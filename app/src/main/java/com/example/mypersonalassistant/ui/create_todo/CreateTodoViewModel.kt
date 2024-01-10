package com.example.mypersonalassistant.ui.create_todo

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypersonalassistant.ui.util.EMPTY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CreateTodoViewModel @Inject constructor(
    application: Application,
    private val todosRepository: TodosRepository
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

    private val addTodoProcessing = MutableStateFlow(false)

    val saveButtonEnabled: StateFlow<Boolean> by lazy {
        combine(
            addTodoProcessing,
            todoName
        ) { processing, name -> !processing && name.isNotEmpty() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)
    }

    private val _showProgressBar = mutableStateOf(false)
    val showProgressBar: State<Boolean> = _showProgressBar

    private val _closeScreen = MutableSharedFlow<Unit>()
    val closeScreen: Flow<Unit> = _closeScreen.asSharedFlow()

    fun onSaveTodoClicked() {
        viewModelScope.launch {
            addTodoProcessing.value = true
            _showProgressBar.value = true
            todosRepository.addTodo(todoName.value, todoTasks.value)
            _showProgressBar.value = false
            addTodoProcessing.value = false
            _closeScreen.emit(Unit)
        }
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