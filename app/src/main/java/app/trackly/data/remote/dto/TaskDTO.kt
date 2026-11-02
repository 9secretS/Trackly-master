package app.trackly.data.remote.dto

import app.trackly.domain.model.Task
import app.trackly.domain.model.TaskPriority
import kotlinx.serialization.Serializable

@Serializable
data class TaskRequestDto(
    val content: String,
    val priority: String,
    val isCompleted: Boolean
)

@Serializable
data class TaskResponseDto(
    val id: Long,
    val sphereId: Long,
    val content: String,
    val priority: String,
    val isCompleted: Boolean
)

fun TaskResponseDto.toDomain(): Task {
    return Task(
        id = id,
        sphereId = sphereId,
        content = content,
        priority = priority.toTaskPriority(),
        isCompleted = isCompleted
    )
}

fun Task.toRequestDto(): TaskRequestDto {
    return TaskRequestDto(
        content = content,
        priority = priority.name,
        isCompleted = isCompleted
    )
}

private fun String.toTaskPriority(): TaskPriority {
    return when (uppercase()) {
        "LOW" -> TaskPriority.LOW
        "MEDIUM" -> TaskPriority.MEDIUM
        "HIGH" -> TaskPriority.HIGH
        else -> TaskPriority.LOW
    }
}