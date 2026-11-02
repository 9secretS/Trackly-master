package app.trackly.domain.use_cases.task_use_cases

import app.trackly.domain.model.Task
import app.trackly.domain.repository.TaskRepository

class GetTask(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(sphereId: Long, taskId: Long): Task? {
        return repository.getTask(sphereId, taskId)
    }
}