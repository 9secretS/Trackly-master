package app.trackly.domain.repository

import app.trackly.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun getTask(sphereId: Long, taskId: Long): Task?

    fun getAllTasks(): Flow<List<Task>>

    fun getTasksBySphere(sphereId: Long): Flow<List<Task>>

    suspend fun insertTask(task: Task)

    suspend fun deleteTask(task: Task)

    suspend fun updateTask(task: Task)
}