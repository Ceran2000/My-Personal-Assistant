package com.example.mypersonalassistant.ui.create_task_list

import com.example.mypersonalassistant.auth.AuthManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TaskListRepository @Inject constructor(
    private val database: FirebaseFirestore,
    private val authManager: AuthManager
) {
    suspend fun addTaskList(title: String, tasks: List<Task>) {
        val input = hashMapOf(
            "title" to title,
            "tasks" to tasks.map { it.newContentState },
            "userId" to authManager.userId
        )
        database.collection("TaskList").add(input).await()
    }
}