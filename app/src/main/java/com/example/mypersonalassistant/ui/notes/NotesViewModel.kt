package com.example.mypersonalassistant.ui.notes

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypersonalassistant.data.model.Note
import com.example.mypersonalassistant.data.repository.OfflineFirstNoteRepository
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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteRepository: OfflineFirstNoteRepository,
    application: Application
) : AndroidViewModel(application) {

    val notes: StateFlow<List<Note>> by lazy {
        noteRepository.getNotes()      //TODO: error catch
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    }

/*    private suspend fun getNotes(): List<RemoteNote> {
        showProgressBar()
        val notes = try {
            noteRepository.getAllNotesForUser()
        } catch (e: Exception) {
            e.toLocalizedException().message?.also { showToast(it) }
            emptyList()
        }
        hideProgressBar()
        return notes
    }*/

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
                noteRepository.addNote(noteTitle.value, noteContent.value)
            } catch (e: Exception) {
                e.toLocalizedException().message?.also { showToast(it) }
            }
            _noteTitle.value = String.EMPTY
            _noteContent.value = String.EMPTY
            hideProgressBar()
            addNoteProcessing.value = false
        }
    }

    fun removeNote(note: Note) {
        viewModelScope.launch {
            showProgressBar()
            try {
                noteRepository.removeNote(note.id)
            } catch (e: Exception) {
                e.toLocalizedException().message?.also { showToast(it) }
            }
            hideProgressBar()
        }
    }

    private val _showProgressBar = mutableStateOf(false)
    val showProgressBar: State<Boolean> = _showProgressBar

    private fun showProgressBar() {
        _showProgressBar.value = true
    }

    private fun hideProgressBar() {
        _showProgressBar.value = false
    }

}