package app.trackly.domain.repository

interface AuthRepository {

    suspend fun register(
        login: String,
        email: String,
        password: String
    )

    suspend fun login(
        email: String,
        password: String
    )

    suspend fun logout()

    suspend fun isAuthorized(): Boolean
}