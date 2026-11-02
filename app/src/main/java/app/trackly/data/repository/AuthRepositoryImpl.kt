package app.trackly.data.repository

import app.trackly.data.local.TokenManager
import app.trackly.data.remote.TracklyApi
import app.trackly.data.remote.dto.LoginRequestDto
import app.trackly.data.remote.dto.RegisterRequestDto
import app.trackly.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val api: TracklyApi,
    private val tokenManager: TokenManager
) : AuthRepository {
    override suspend fun register(login: String, email: String, password: String) {
        api.register(
            RegisterRequestDto(
                login = login,
                email = email,
                password = password
            )
        )
    }

    override suspend fun login(email: String, password: String) {
        val response = api.login(
            LoginRequestDto(
                email = email,
                password = password
            )
        )

        tokenManager.saveToken(response.token)
    }

    override suspend fun logout() {
        tokenManager.clearToken()
    }

    override suspend fun isAuthorized(): Boolean {
        return !tokenManager.getToken().isNullOrBlank()
    }
}