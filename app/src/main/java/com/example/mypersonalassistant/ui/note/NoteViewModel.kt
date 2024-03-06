package com.example.mypersonalassistant.ui.note

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.mypersonalassistant.data.note.NotesRepository
import com.example.mypersonalassistant.model.Note
import com.example.mypersonalassistant.ui.util.UiState
import com.example.mypersonalassistant.ui.util.showToast
import com.example.mypersonalassistant.ui.util.toLocalizedException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val notesRepository: NotesRepository
) : AndroidViewModel(application) {

    private val noteId: String = checkNotNull(savedStateHandle[noteIdArg])

    private val _uiState = MutableStateFlow<UiState<Note>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private fun loadNote() {
        viewModelScope.launch {
            try {
                val data = notesRepository.getNoteById(noteId)
                _uiState.emit(UiState.ShowContent(data))
            } catch (e: Exception) {
                _uiState.emit(UiState.Empty)
                e.toLocalizedException().message?.also {
                    showToast(it)
                }
            }
        }
    }

    init {
        loadNote()
    }

}