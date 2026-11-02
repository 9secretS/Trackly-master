package app.trackly.data.repository

import app.trackly.data.remote.TracklyApi
import app.trackly.data.remote.dto.toDomain
import app.trackly.data.remote.dto.toRequestDto
import app.trackly.domain.model.Task
import app.trackly.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val api: TracklyApi
) : TaskRepository {

    private val tasksBySphere = mutableMapOf<Long, MutableStateFlow<List<Task>>>()

    override fun getTasksBySphere(sphereId: Long): Flow<List<Task>> = flow {
        val flow = getOrCreateTasksFlow(sphereId)

        refreshTasksBySphere(sphereId)

        emitAll(flow)
    }

    override fun getAllTasks(): Flow<List<Task>> = flow {
        val spheres = api.getSpheres()

        val allTasks = spheres.flatMap { sphere ->
            api.getTasksBySphere(sphere.id).map { it.toDomain() }
        }

        emit(allTasks)
    }

    override suspend fun getTask(
        sphereId: Long,
        taskId: Long
    ): Task? {
        return api.getTask(
            sphereId = sphereId,
            taskId = taskId
        ).toDomain()
    }

    override suspend fun insertTask(task: Task) {
        val createdTask = api.createTask(
            sphereId = task.sphereId,
            request = task.toRequestDto()
        ).toDomain()

        val flow = getOrCreateTasksFlow(task.sphereId)

        flow.value = flow.value + createdTask
    }

    override suspend fun deleteTask(task: Task) {
        api.deleteTask(
            sphereId = task.sphereId,
            taskId = task.id
        )

        val flow = getOrCreateTasksFlow(task.sphereId)

        flow.value = flow.value.filterNot {
            it.id == task.id
        }
    }

    override suspend fun updateTask(task: Task) {
        val updatedTask = api.updateTask(
            sphereId = task.sphereId,
            taskId = task.id,
            request = task.toRequestDto()
        ).toDomain()

        val flow = getOrCreateTasksFlow(task.sphereId)

        flow.value = flow.value.map {
            if (it.id == updatedTask.id) updatedTask else it
        }
    }

    private fun getOrCreateTasksFlow(
        sphereId: Long
    ): MutableStateFlow<List<Task>> {
        return tasksBySphere.getOrPut(sphereId) {
            MutableStateFlow(emptyList())
        }
    }

    private suspend fun refreshTasksBySphere(sphereId: Long) {
        val flow = getOrCreateTasksFlow(sphereId)

        flow.value = api.getTasksBySphere(sphereId).map {
            it.toDomain()
        }
    }
}