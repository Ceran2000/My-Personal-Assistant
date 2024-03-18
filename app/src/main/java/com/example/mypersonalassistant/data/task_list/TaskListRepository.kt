package com.example.mypersonalassistant.data.task_list

import com.example.mypersonalassistant.auth.AuthManager
import com.example.mypersonalassistant.firestore.Constants
import com.example.mypersonalassistant.ui.create_task_list.Task
import com.example.mypersonalassistant.model.TaskList
import com.example.mypersonalassistant.model.toTaskList
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TaskListRepository @Inject constructor(
    private val database: FirebaseFirestore,
    private val authManager: AuthManager
) {
    suspend fun addTaskList(title: String, tasks: List<Task>) {
        val input = hashMapOf(
            "title" to title,
            "tasks" to tasks.filter { it.isNotEmpty }.map { task ->
                hashMapOf(
                    "content" to task.newContent,
                    "endTimeMillis" to task.newEndDateMillis
                )
            },
            "userId" to authManager.userId,
            "addedAt" to FieldValue.serverTimestamp()
        )
        database.collection(Constants.COLLECTION_TASK_LIST).add(input).await()
    }

    suspend fun getAllTaskListsForUser(): List<TaskList> {
        val data = database
            .collection(Constants.COLLECTION_TASK_LIST)
            .whereEqualTo("userId", authManager.userId)
            .orderBy("addedAt", Query.Direction.DESCENDING)
            .get()
            .await()

        return data.map { it.toTaskList() }
    }

    suspend fun getTaskListById(taskListId: String): TaskList {
        val data = database
            .collection(Constants.COLLECTION_TASK_LIST)
            .document(taskListId)
            .get()
            .await()

        return data.toTaskList()

    }

    suspend fun getLatestTaskListForUser(): TaskList? {
        val data = database
            .collection(Constants.COLLECTION_TASK_LIST)
            .whereEqualTo("userId", authManager.userId)
            .orderBy("addedAt", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()

        return data.singleOrNull()?.toTaskList()
    }

    suspend fun updateTaskList(id: String, title: String, tasks: List<Task>) {
        val taskListRef = database
            .collection(Constants.COLLECTION_TASK_LIST)
            .document(id)

        val input = hashMapOf(
            "title" to title,
            "tasks" to tasks.filter { it.isNotEmpty }.map { task ->
                hashMapOf(
                    "content" to task.newContent,
                    "endTimeMillis" to task.newEndDateMillis
                )
            }
        )

        taskListRef.update(input).await()
    }
}