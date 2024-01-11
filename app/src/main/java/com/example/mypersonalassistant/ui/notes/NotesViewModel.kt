package com.example.mypersonalassistant.ui.notes

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypersonalassistant.data.note.NotesRepository
import com.example.mypersonalassistant.model.Note
import com.example.mypersonalassistant.ui.util.EMPTY
import com.example.mypersonalassistant.ui.util.showToast
import com.example.mypersonalassistant.ui.util.toLocalizedException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val notesRepository: NotesRepository,
    application: Application
) : AndroidViewModel(application) {

    private val refreshNotes = MutableSharedFlow<Unit>()
    val notes: StateFlow<List<Note>> by lazy {
        refreshNotes
            .onStart { emit(Unit) }
            .map { getNotes() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    }

    private suspend fun getNotes(): List<Note> {
        showProgressBar()
        val notes = try {
            notesRepository.getNotesForUser()
        } catch (e: Exception) {
            e.toLocalizedException().message?.also { showToast(it) }
            emptyList()
        }
        hideProgressBar()
        return notes
    }

    private val _noteTitle = MutableStateFlow(String.EMPTY)
    val noteTitle: StateFlow<String> = _noteTitle.asStateFlow()

    fun onTitleChanged(title: String) {
        _noteTitle.value = title
    }

    private val _noteContent = MutableStateFlow(String.EMPTY)
    val noteContent: StateFlow<String> = _noteContent.asStateFlow()

    fun onContentChanged(content: String) {
        _noteContent.value = content
    }

    private val addNoteProcessing = MutableStateFlow(false)
    val addNoteButtonEnabled: StateFlow<Boolean> by lazy {
        combine(
            addNoteProcessing,
            noteTitle.map { it.isNotEmpty() },
            noteContent.map { it.isNotEmpty() }
        ) { t1, t2, t3 -> !t1 && t2 && t3 }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)
    }

    fun onAddNoteClicked() {
        viewModelScope.launch {
            addNoteProcessing.value = true
            showProgressBar()
            try {
                notesRepository.addNote(noteTitle.value, noteContent.value)
            } catch (e: Exception) {
                e.toLocalizedException().message?.also { showToast(it) }
            }
            _noteTitle.value = String.EMPTY
            _noteContent.value = String.EMPTY
            refreshNotes()
            hideProgressBar()
            addNoteProcessing.value = false
        }
    }

    fun removeNote(note: Note) {
        viewModelScope.launch {
            showProgressBar()
            try {
                notesRepository.removeNote(note)
            } catch (e: Exception) {
                e.toLocalizedException().message?.also { showToast(it) }
            }
            refreshNotes()
            hideProgressBar()
        }
    }

    private suspend fun refreshNotes() = refreshNotes.emit(Unit)

    private val _showProgressBar = mutableStateOf(false)
    val showProgressBar: State<Boolean> = _showProgressBar

    private fun showProgressBar() {
        _showProgressBar.value = true
    }

    private fun hideProgressBar() {
        _showProgressBar.value = false
    }

}