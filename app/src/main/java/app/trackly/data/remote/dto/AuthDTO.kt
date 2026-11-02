package app.trackly.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    val email: String,
    val password: String
)

@Serializable
data class LoginResponseDto(
    val token: String
)

@Serializable
data class RegisterRequestDto(
    val login: String,
    val email: String,
    val password: String
)

@Serializable
data class RegisterResponseDto(
    val id: Long,
    val email: String
)