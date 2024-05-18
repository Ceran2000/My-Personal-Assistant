package com.example.mypersonalassistant.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mypersonalassistant.data.model.Note

@Entity(tableName = "note")
data class LocalNote(
    @PrimaryKey val id: String,
    val title: String,
    val content: String,
    val addedAtMillis: Long
)

fun LocalNote.asExternalModel() = Note(id, title, content)