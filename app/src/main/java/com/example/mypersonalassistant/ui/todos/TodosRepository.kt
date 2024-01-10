package com.example.mypersonalassistant.ui.todos

import com.example.mypersonalassistant.auth.AuthManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TodosRepository @Inject constructor(
    private val database: FirebaseFirestore,
    private val authManager: AuthManager
) {
    suspend fun getTodosForUser(): List<Todo> {
        val data = database.collection("todo").whereEqualTo("userId", authManager.userId!!).get().await()
        return data.map { it.toTodo() }
    }
}