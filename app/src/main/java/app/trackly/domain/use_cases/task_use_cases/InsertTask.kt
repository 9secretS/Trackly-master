package app.trackly.domain.use_cases.task_use_cases

import app.trackly.domain.model.Task
import app.trackly.domain.repository.TaskRepository

class InsertTask(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task) {
        repository.insertTask(task)
    }
}