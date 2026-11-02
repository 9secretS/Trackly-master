package app.trackly.domain.use_cases.task_use_cases

import app.trackly.domain.model.Task
import app.trackly.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTasksBySphere(
    private val repository: TaskRepository
) {
    operator fun invoke(sphereId: Long, order: String = "default"): Flow<List<Task>> {
        return repository.getTasksBySphere(sphereId).map { tasks ->
            when (order) {
                "low_to_high" -> tasks.sortedBy { it.priority }
                "high_to_low" -> tasks.sortedByDescending { it.priority }
                else -> tasks
            }
        }
    }
}