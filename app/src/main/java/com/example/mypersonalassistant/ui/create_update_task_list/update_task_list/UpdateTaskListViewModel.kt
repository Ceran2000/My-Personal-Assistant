package com.example.mypersonalassistant.ui.create_update_task_list.update_task_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.mypersonalassistant.data.remote.model.Task
import com.example.mypersonalassistant.data.remote.repository.TaskListRepository
import com.example.mypersonalassistant.data.remote.model.TaskList
import com.example.mypersonalassistant.ui.util.showToast
import com.example.mypersonalassistant.ui.util.toLocalizedException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
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
            processingFlow.emit(true)
            try {
                val data = taskListRepository.getTaskListById(taskListId)
                taskList.value = data
            } catch (e: Exception) {
                e.toLocalizedException().message?.also {
                    showToast(it)
                }
                _closeScreen.emit(Unit)
            }
            processingFlow.emit(false)
        }
    }

    private val taskContentValueChanged = MutableSharedFlow<Unit>()
    fun onTaskContentValueChanged() {
        viewModelScope.launch {
            taskContentValueChanged.emit(Unit)
        }
    }

    private val processingFlow = MutableStateFlow(false)
    val showProgressBar = processingFlow.asStateFlow()

    private val _closeScreen = MutableSharedFlow<Unit>()
    val closeScreen: Flow<Unit> = _closeScreen.asSharedFlow()

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

    val saveButtonEnabled = combine(
        processingFlow,
        listTitle,
        tasks,
        taskContentValueChanged.onStart { emit(Unit) }
    ) { processing, name, taskList, _ ->
        !processing && name.isNotEmpty() && taskList.isNotEmpty() && taskList.all { it.isNotEmpty }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    fun onSaveButtonClicked() {
        viewModelScope.launch {
            processingFlow.emit(true)
            try {
                taskListRepository.updateTaskList(
                    id = taskListId,
                    title = listTitle.value,
                    tasks = tasks.value
                )
                _closeScreen.emit(Unit)
            } catch (e: Exception) {
                e.toLocalizedException().message?.also {
                    showToast(it)
                }
            }
            processingFlow.emit(false)
        }
    }

    init {
        loadTaskList()
    }
}