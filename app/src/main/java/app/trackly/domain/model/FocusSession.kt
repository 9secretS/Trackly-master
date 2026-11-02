package app.trackly.domain.model

data class FocusSession(
    val id: Long,
    val sphereId: Long,
    val title: String,
    val comment: String?,
    val status: SessionStatus,
    val planDurationSeconds: Int,
    val factualDurationSeconds: Int?,
    val pausedSeconds: Int,
    val startedAt: String?,
    val endedAt: String?
)
