package com.example.mypersonalassistant.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypersonalassistant.auth.AuthManager
import com.example.mypersonalassistant.auth.User
import com.example.mypersonalassistant.data.note.NotesRepository
import com.example.mypersonalassistant.data.task_list.TaskListRepository
import com.example.mypersonalassistant.model.Note
import com.example.mypersonalassistant.model.TaskList
import com.example.mypersonalassistant.ui.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authManager: AuthManager,
    private val notesRepository: NotesRepository,
    private val taskListRepository: TaskListRepository
) : ViewModel() {

    val currentUser: StateFlow<User?> = authManager.currentUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    val greetingMessage: StateFlow<String> = currentUser.map { user ->
        val hello = "Cześć"
        if (user != null) "$hello, ${user.displayValue}!" else "$hello!"
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "Cześć")

    val latestNote: StateFlow<UiState<Note>> = flow {
        val note = notesRepository.getLatestNoteForUser()
        val uiState = note?.let { UiState.ShowContent(it) } ?: UiState.Empty
        emit(uiState)
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), UiState.Loading)

    val latestTaskList: StateFlow<UiState<TaskList>> = flow {
        val taskList = taskListRepository.getLatestTaskListForUser()
        val uiState = taskList?.let { UiState.ShowContent(it) } ?: UiState.Empty
        emit(uiState)
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), UiState.Loading)

    fun signOut() {
        viewModelScope.launch {
            authManager.signOut()
        }
    }

}