package app.trackly.domain.model

data class Task(
    val id: Long,
    val sphereId: Long,
    val content: String,
    val priority: TaskPriority,
    val isCompleted: Boolean
)