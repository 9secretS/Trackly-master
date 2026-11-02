package app.trackly.domain.use_cases.task_use_cases

import app.trackly.domain.model.Task
import app.trackly.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllTasks(
    private val repository: TaskRepository
) {
    operator fun invoke(order: String = "default"): Flow<List<Task>> {
        return repository.getAllTasks().map { tasks ->
            when (order) {
                "low_to_high" -> tasks.sortedBy { it.priority }
                "high_to_low" -> tasks.sortedByDescending { it.priority }
                else -> tasks
            }
        }
    }
}