package com.example.mypersonalassistant.data.repository

import com.example.mypersonalassistant.data.local.dao.NoteDao
import com.example.mypersonalassistant.data.local.entities.LocalNote
import com.example.mypersonalassistant.data.local.entities.asExternalModel
import com.example.mypersonalassistant.data.model.Note
import com.example.mypersonalassistant.data.remote.model.RemoteNote
import com.example.mypersonalassistant.data.remote.model.asEntity
import com.example.mypersonalassistant.data.remote.repository.NoteRemoteDataSource
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class OfflineFirstNoteRepository @Inject constructor(
    private val noteDao: NoteDao,
    private val remoteDateSource: NoteRemoteDataSource
) {
    private val scope = MainScope()

    fun getNotes(): Flow<List<Note>> =
        noteDao.getAll()
            .map { it.map(LocalNote::asExternalModel) }
            .onStart {
                scope.launch { refreshNotes() }
            }

    suspend fun getNote(id: String): Note =
        noteDao.findById(id)?.asExternalModel()!!

    fun getLatestNote(): Flow<Note?> =
        noteDao.getLatest().map { it?.asExternalModel() }
            .onStart { scope.launch { refreshNotes() } }

    suspend fun addNote(title: String, content: String) {
        remoteDateSource.addNote(title, content)
        refreshNotes()      //TODO: handle timeout
    }

    suspend fun removeNote(id: String) {
        remoteDateSource.removeNote(id)
        refreshNotes()
    }

    suspend fun refreshNotes() {
        delay(5000)
        val remoteNotes = remoteDateSource.getAllNotesForUser().map(RemoteNote::asEntity)
        noteDao.syncAll(remoteNotes)
    }
}