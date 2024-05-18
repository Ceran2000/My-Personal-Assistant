package com.example.mypersonalassistant.data.remote.repository

import com.example.mypersonalassistant.auth.AuthManager
import com.example.mypersonalassistant.firestore.Constants
import com.example.mypersonalassistant.data.remote.model.RemoteNote
import com.example.mypersonalassistant.data.remote.model.toNote
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NoteRemoteDataSource @Inject constructor(
    private val database: FirebaseFirestore,
    private val authManager: AuthManager
) {

    suspend fun getAllNotesForUser(): List<RemoteNote> {
        val data = database
            .collection(Constants.COLLECTION_NOTE)
            .whereEqualTo("userId", authManager.userId)
            .get()
            .await()

        return data.map { it.toNote() }
    }

    suspend fun getLatestNoteForUser(): RemoteNote? {
        val data = database
            .collection(Constants.COLLECTION_NOTE)
            .whereEqualTo("userId", authManager.userId)
            .orderBy("addedAt", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()

        return data.singleOrNull()?.toNote()
    }

    suspend fun getNoteById(noteId: String): RemoteNote {
        val data = database
            .collection(Constants.COLLECTION_NOTE)
            .document(noteId)
            .get()
            .await()

        return data.toNote()
    }

    suspend fun addNote(title: String, content: String) {
        val input = hashMapOf(
            "title" to title,
            "content" to content,
            "userId" to authManager.userId,
            "addedAt" to FieldValue.serverTimestamp()
        )
        database.collection(Constants.COLLECTION_NOTE).add(input).await()
    }

    suspend fun removeNote(id: String) {
        database.collection(Constants.COLLECTION_NOTE).document(id).delete().await()
    }
}