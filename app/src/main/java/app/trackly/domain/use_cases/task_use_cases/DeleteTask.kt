package app.trackly.domain.use_cases.task_use_cases

import app.trackly.domain.model.Task
import app.trackly.domain.repository.TaskRepository

class DeleteTask(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task) {
        repository.deleteTask(task)
    }
}