package com.example.mypersonalassistant.ui.update_task_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mypersonalassistant.data.task_list.TaskListRepository
import com.example.mypersonalassistant.model.TaskList
import com.example.mypersonalassistant.ui.create_task_list.Task
import com.example.mypersonalassistant.ui.util.showToast
import com.example.mypersonalassistant.ui.util.toLocalizedException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateTaskListViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val taskListRepository: TaskListRepository
) : AndroidViewModel(application) {

    private val taskListId: String = checkNotNull(savedStateHandle[taskListIdArg])

    private val taskList = MutableStateFlow<TaskList?>(null)

    private fun loadTaskList() {
        viewModelScope.launch {
            try {
                val data = taskListRepository.getTaskListById(taskListId)
                taskList.value = data
            } catch (e: Exception) {
                //TODO: close screen
                e.toLocalizedException().message?.also {
                    showToast(it)
                }
            }
        }
    }

    init {
        loadTaskList()
    }

    private val newTitle = MutableSharedFlow<String>()
    fun onTitleValueChanged(value: String) {
        viewModelScope.launch {
            newTitle.emit(value)
        }
    }

    val listTitle = merge(
        newTitle,
        taskList.filterNotNull().map { it.title }
    )
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "")

    private val _tasks = MutableSharedFlow<List<Task>>()
    fun onAddTaskClicked() {
        viewModelScope.launch {
            val list = tasks.value.toMutableList()
            list.add(Task.empty())
            _tasks.emit(list)
        }
    }

    fun onRemoveTaskClicked(index: Int) {
        viewModelScope.launch {
            val list = tasks.value.toMutableList()
            list.removeAt(index)
            _tasks.emit(list)
        }
    }

    val tasks = merge(
        _tasks,
        taskList
            .filterNotNull()
            .map { it.tasks }
    )
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun onSaveButtonClicked() {
        viewModelScope.launch {
            try {
                taskListRepository.updateTaskList(
                    id = taskListId,
                    title = listTitle.value,
                    tasks = tasks.value
                )
            } catch (e: Exception) {
                e.toLocalizedException().message?.also {
                    showToast(it)
                }
            }
        }
    }
}