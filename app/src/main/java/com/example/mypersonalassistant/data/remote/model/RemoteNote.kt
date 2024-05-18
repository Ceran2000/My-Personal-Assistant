package com.example.mypersonalassistant.data.remote.model

import com.example.mypersonalassistant.data.local.entities.LocalNote
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot

data class RemoteNote(val id: String, val title: String, val content: String, val addedAtTimestamp: Timestamp)

fun DocumentSnapshot.toNote(): RemoteNote = RemoteNote(
    id = this.id,
    title = this["title"] as String,
    content = this["content"] as String,
    addedAtTimestamp = this["addedAt"] as Timestamp
)

fun RemoteNote.asEntity(): LocalNote = LocalNote(id, title, content, addedAtMillis = addedAtTimestamp.toDate().time)