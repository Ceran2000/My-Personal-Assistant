package com.example.mypersonalassistant.ui.todos

import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.firebase.firestore.QueryDocumentSnapshot

data class Todo(val id: String, val title: String, val tasks: List<String>) {

    @Composable
    fun Item(modifier: Modifier = Modifier) {
        ElevatedCard(modifier) {
            ListItem(headlineContent = { Text(title) })
        }
    }
}

fun QueryDocumentSnapshot.toTodo(): Todo = Todo(
    id = this.id,
    title = this["title"] as String,
    tasks = this["tasks"] as List<String>
)