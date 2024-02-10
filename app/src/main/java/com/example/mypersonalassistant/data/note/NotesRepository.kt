package com.example.mypersonalassistant.data.note

import com.example.mypersonalassistant.auth.AuthManager
import com.example.mypersonalassistant.firestore.Constants
import com.example.mypersonalassistant.model.Note
import com.example.mypersonalassistant.model.toNote
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotesRepository @Inject constructor(
    private val database: FirebaseFirestore,
    private val authManager: AuthManager
) {

    suspend fun getNotesForUser(): List<Note> {
        val data = database.collection(Constants.COLLECTION_NOTE).whereEqualTo("userId", authManager.userId).get().await()
        return data.map { it.toNote() }
    }

    suspend fun addNote(title: String, content: String) {
        val input = hashMapOf(
            "title" to title,
            "content" to content,
            "userId" to authManager.userId
        )
        database.collection(Constants.COLLECTION_NOTE).add(input).await()
    }

    suspend fun removeNote(note: Note) {
        database.collection(Constants.COLLECTION_NOTE).document(note.id).delete().await()
    }
}