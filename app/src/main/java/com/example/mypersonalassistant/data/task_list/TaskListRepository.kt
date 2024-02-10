package com.example.mypersonalassistant.data.task_list

import com.example.mypersonalassistant.auth.AuthManager
import com.example.mypersonalassistant.firestore.Constants
import com.example.mypersonalassistant.ui.create_task_list.Task
import com.example.mypersonalassistant.model.TaskList
import com.example.mypersonalassistant.model.toTaskList
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
            "tasks" to tasks.map { it.newContent },
            "userId" to authManager.userId
        )
        database.collection(Constants.COLLECTION_TASK_LIST).add(input).await()
    }

    suspend fun getTaskListsForUser(): List<TaskList> {
            val data = database.collection(Constants.COLLECTION_TASK_LIST).whereEqualTo("userId", authManager.userId).get().await()
            return data.map { it.toTaskList() }
        }
}