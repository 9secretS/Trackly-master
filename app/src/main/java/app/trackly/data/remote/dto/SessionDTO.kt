package app.trackly.data.remote.dto

import app.trackly.domain.model.FocusSession
import app.trackly.domain.model.SessionStatus
import kotlinx.serialization.Serializable

@Serializable
data class CreateSessionRequestDto(
    val title: String,
    val planDurationSeconds: Int
)

@Serializable
data class FinishSessionRequestDto(
    val comment: String?
)

@Serializable
data class SessionResponseDto(
    val id: Long,
    val sphereId: Long,
    val title: String,
    val comment: String?,
    val status: String,
    val planDurationSeconds: Int,
    val factualDurationSeconds: Int?,
    val pausedSeconds: Int,
    val startedAt: String?,
    val endedAt: String?
)

fun SessionResponseDto.toDomain(): FocusSession {
    return FocusSession(
        id = id,
        sphereId = sphereId,
        title = title,
        comment = comment,
        status = status.toSessionStatus(),
        planDurationSeconds = planDurationSeconds,
        factualDurationSeconds = factualDurationSeconds,
        pausedSeconds = pausedSeconds,
        startedAt = startedAt,
        endedAt = endedAt
    )
}

private fun String.toSessionStatus(): SessionStatus {
    return when (uppercase()) {
        "CREATED" -> SessionStatus.CREATED
        "ACTIVE" -> SessionStatus.ACTIVE
        "PAUSED" -> SessionStatus.PAUSED
        "COMPLETED" -> SessionStatus.COMPLETED
        "CANCELED" -> SessionStatus.CANCELED
        else -> SessionStatus.CREATED
    }
}