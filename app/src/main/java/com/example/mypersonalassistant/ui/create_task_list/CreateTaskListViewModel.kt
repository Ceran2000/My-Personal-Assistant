package com.example.mypersonalassistant.ui.create_task_list

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypersonalassistant.data.task_list.TaskListRepository
import com.example.mypersonalassistant.ui.util.EMPTY
import com.example.mypersonalassistant.ui.util.showToast
import com.example.mypersonalassistant.ui.util.toLocalizedException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
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
class CreateTaskListViewModel @Inject constructor(
    application: Application,
    private val taskListRepository: TaskListRepository
) : AndroidViewModel(application) {

    private val _listName = MutableStateFlow(String.EMPTY)
    val listName: StateFlow<String> = _listName.asStateFlow()

    fun onListNameValueChanged(value: String) {
        _listName.value = value
    }

    private val taskContentValueChanged = MutableSharedFlow<Unit>()
    fun onTaskContentValueChanged() {
        viewModelScope.launch {
            taskContentValueChanged.emit(Unit)
        }
    }

    private val _tasks = MutableStateFlow(listOf(Task.empty()))
    val tasks = _tasks.asStateFlow()

    fun onAddTaskClicked() {
        _tasks.update {
            val list = it.toMutableList()
            list.add(Task.empty())
            list
        }
    }

    fun onDeleteTaskClicked(index: Int) {
        _tasks.update {
            val list = it.toMutableList()
            list.removeAt(index)
            list
        }
    }

    private val processingFlow = MutableStateFlow(false)

    val saveButtonEnabled: StateFlow<Boolean> by lazy {
        combine(
            processingFlow,
            listName,
            tasks,
            taskContentValueChanged
        ) { processing, name, taskList,_ ->
            !processing && name.isNotEmpty() && taskList.isNotEmpty() && taskList.all { it.isNotEmpty }
        }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)
    }

    private val _showProgressBar = mutableStateOf(false)
    val showProgressBar: State<Boolean> = _showProgressBar

    private val _closeScreen = MutableSharedFlow<Unit>()
    val closeScreen: Flow<Unit> = _closeScreen.asSharedFlow()

    fun onSaveListClicked() {
        viewModelScope.launch {
            processingFlow.value = true
            _showProgressBar.value = true
            try {
                taskListRepository.addTaskList(listName.value, tasks.value)
            } catch (e: Exception) {
                e.toLocalizedException().message?.also {
                    showToast(it)
                }
            }
            _showProgressBar.value = false
            processingFlow.value = false
            _closeScreen.emit(Unit)
        }
    }
}

data class Task(val content: String) {

    var newContent by mutableStateOf(content)

    val contentChanged get() = newContent != content
    val isNotEmpty get() = newContent.isNotEmpty()

    companion object {
        fun empty() = Task(String.EMPTY)
    }
}