package app.trackly.data.remote.dto

import app.trackly.domain.model.Sphere
import kotlinx.serialization.Serializable

@Serializable
data class SphereRequestDto(
    val title: String,
    val colorKey: String,
    val iconKey: String,
    val hasTasks: Boolean
)

@Serializable
data class SphereResponseDto(
    val id: Long,
    val title: String,
    val colorKey: String,
    val iconKey: String,
    val hasTasks: Boolean
)

fun SphereResponseDto.toDomain(): Sphere {
    return Sphere(
        id = id,
        title = title,
        colorKey = colorKey,
        iconKey = iconKey,
        hasTasks = hasTasks
    )
}

fun Sphere.toRequestDto(): SphereRequestDto {
    return SphereRequestDto(
        title = title,
        colorKey = colorKey,
        iconKey = iconKey,
        hasTasks = hasTasks
    )
}
