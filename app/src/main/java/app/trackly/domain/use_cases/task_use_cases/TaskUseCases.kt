package app.trackly.domain.use_cases.task_use_cases

data class TaskUseCases(
    val getAllTasks: GetAllTasks,
    val getTasksBySphere: GetTasksBySphere,
    val getTask: GetTask,
    val insertTask: InsertTask,
    val deleteTask: DeleteTask,
    val updateTask: UpdateTask
)
