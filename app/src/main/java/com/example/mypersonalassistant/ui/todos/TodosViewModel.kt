package com.example.mypersonalassistant.ui.todos

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TodosViewModel @Inject constructor(
    application: Application,
    private val todosRepository: TodosRepository
) : AndroidViewModel(application) {

    val todos: StateFlow<List<Todo>> by lazy {
        flow {
            val list = todosRepository.getTodosForUser()
            emit(list)
        }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    }

}