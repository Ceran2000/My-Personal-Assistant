package com.example.mypersonalassistant.ui.task_lists

import com.example.mypersonalassistant.auth.AuthManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TaskListRepository @Inject constructor(
    private val database: FirebaseFirestore,
    private val authManager: AuthManager
) {
    suspend fun getTodosForUser(): List<TaskList> {
        val data = database.collection("todo").whereEqualTo("userId", authManager.userId).get().await()
        return data.map { it.toTaskList() }
    }
}