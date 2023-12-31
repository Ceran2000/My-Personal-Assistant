package com.example.mypersonalassistant.ui.notes

import com.example.mypersonalassistant.firestore.Constants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotesRepository @Inject constructor(
    private val database: FirebaseFirestore
) {

    suspend fun getNotes(): List<Note> {
        val data = database.collection(Constants.COLLECTION_NOTES).get().await()
        return data.map { it.toNote() }
    }

    suspend fun addNote(title: String, content: String) {
        val input: HashMap<String, String> = HashMap()
        input["title"] = title
        input["content"] = content
        database.collection(Constants.COLLECTION_NOTES).add(input).await()
    }

    suspend fun removeNote(note: Note) {
        database.collection(Constants.COLLECTION_NOTES).document(note.id).delete().await()
    }
}