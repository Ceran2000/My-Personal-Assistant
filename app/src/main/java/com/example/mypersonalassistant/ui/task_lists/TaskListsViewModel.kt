package com.example.mypersonalassistant.ui.task_lists

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypersonalassistant.data.task_list.TaskListRepository
import com.example.mypersonalassistant.model.TaskList
import com.example.mypersonalassistant.ui.util.showToast
import com.example.mypersonalassistant.ui.util.toLocalizedException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TaskListsViewModel @Inject constructor(
    application: Application,
    private val taskListRepository: TaskListRepository
) : AndroidViewModel(application) {

    val taskLists: StateFlow<List<TaskList>> by lazy {
        flow {
            emit(getTaskLists())
        }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    }

    private suspend fun getTaskLists() = try {
        taskListRepository.getAllTaskListsForUser()
    } catch (e: Exception) {
        e.toLocalizedException().message?.also {
            showToast(it)
        }
        emptyList()
    }

}