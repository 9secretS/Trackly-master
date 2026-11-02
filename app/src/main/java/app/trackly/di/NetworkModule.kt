package app.trackly.di

import app.trackly.data.local.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val HOST = "10.0.2.2"
    private const val PORT = 8080

    @Provides
    @Singleton
    fun provideHttpClient(
        tokenManager: TokenManager
    ): HttpClient {
        return HttpClient(Android) {
            expectSuccess = true

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                        encodeDefaults = true
                    }
                )
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val token = tokenManager.getToken()

                        if (token.isNullOrBlank()) {
                            null
                        } else {
                            BearerTokens(
                                accessToken = token,
                                refreshToken = ""
                            )
                        }
                    }

                    sendWithoutRequest { request ->
                        val path = request.url.encodedPath

                        path != "/login" && path != "/register"
                    }
                }
            }

            install(Logging) {
                level = LogLevel.ALL
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 15_000
                connectTimeoutMillis = 15_000
                socketTimeoutMillis = 15_000
            }

            defaultRequest {
                url {
                    protocol = URLProtocol.HTTP
                    host = HOST
                    port = PORT
                }

                contentType(ContentType.Application.Json)
            }
        }
    }
}