package app.trackly.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class WeeklyAnalyticsResponseDto(
    val sphereActivity: List<SphereActivityItemDto>,
    val bestDay: DayStatsDto?,
    val worstDay: DayStatsDto?,
    val averageDaySeconds: Int,
    val longestSession: SessionPreviewDto?,
    val streakDays: Int,
    val weekChart: List<SphereChartLineDto>
)

@Serializable
data class SphereActivityItemDto(
    val sphereId: Long,
    val sphereTitle: String,
    val sphereColor: String,
    val sphereIcon: String,
    val totalSeconds: Int
)

@Serializable
data class DayStatsDto(
    val dayOfWeek: String,
    val totalSeconds: Int
)

@Serializable
data class SessionPreviewDto(
    val sessionId: Long,
    val sphereId: Long,
    val sphereTitle: String,
    val sphereIcon: String,
    val title: String,
    val factualDurationSeconds: Int,
    val endedAt: String,
    val comment: String?
)

@Serializable
data class SphereChartLineDto(
    val sphereId: Long,
    val sphereTitle: String,
    val sphereColor: String,
    val points: List<DayPointDto>
)

@Serializable
data class DayPointDto(
    val dayOfWeek: String,
    val totalSeconds: Int
)