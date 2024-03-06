package com.example.mypersonalassistant.model

import com.google.firebase.firestore.DocumentSnapshot

data class Note(val id: String, val title: String, val content: String) {

    val key = id
}

fun DocumentSnapshot.toNote(): Note = Note(
    id = this.id,
    title = this["title"] as String,
    content = this["content"] as String
)