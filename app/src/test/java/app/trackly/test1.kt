package app.trackly

import app.trackly.domain.model.Task
import app.trackly.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class test1 {
    class FakeTaskRepository : TaskRepository {

        private val tasks = mutableListOf<Task>()
        private val tasksFlow = MutableStateFlow<List<Task>>(emptyList())

        override fun getAllTasks(): Flow<List<Task>> = tasksFlow

        override fun getTasksBySphere(sphereId: Int): Flow<List<Task>> =
            tasksFlow.map { list -> list.filter { it.sphereId == sphereId } }

        override suspend fun getTask(id: Int): Task? =
            tasks.find { it.id == id }

        override suspend fun insertTask(task: Task) {
            val newTask = if (task.id == null) {
                task.copy(id = (tasks.maxOfOrNull { it.id ?: 0 } ?: 0) + 1)
            } else task

            tasks.add(newTask)
            tasksFlow.value = tasks.toList()
        }

        override suspend fun deleteTask(task: Task) {
            tasks.removeIf { it.id == task.id }
            tasksFlow.value = tasks.toList()
        }

        override suspend fun updateTask(task: Task) {
            val index = tasks.indexOfFirst { it.id == task.id }
            if (index != -1) {
                tasks[index] = task
                tasksFlow.value = tasks.toList()
            }
        }
    }
}