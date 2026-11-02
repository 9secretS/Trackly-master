package app.trackly.data.remote

import app.trackly.data.remote.dto.CreateSessionRequestDto
import app.trackly.data.remote.dto.FinishSessionRequestDto
import app.trackly.data.remote.dto.LoginRequestDto
import app.trackly.data.remote.dto.LoginResponseDto
import app.trackly.data.remote.dto.RegisterRequestDto
import app.trackly.data.remote.dto.RegisterResponseDto
import app.trackly.data.remote.dto.SessionResponseDto
import app.trackly.data.remote.dto.SphereRequestDto
import app.trackly.data.remote.dto.SphereResponseDto
import app.trackly.data.remote.dto.TaskRequestDto
import app.trackly.data.remote.dto.TaskResponseDto
import app.trackly.data.remote.dto.WeeklyAnalyticsResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TracklyApi @Inject constructor(
    private val client: HttpClient
) {

    /** Auth **/
    suspend fun login(request: LoginRequestDto): LoginResponseDto {
        return client.post("login") {
            setBody(request)
        }.body()
    }

    suspend fun register(request: RegisterRequestDto): RegisterResponseDto {
        return client.post("register") {
            setBody(request)
        }.body()
    }

    /** Spheres **/
    suspend fun getSpheres(): List<SphereResponseDto> {
        val response = client.get("spheres")

        return response.body()
    }

    suspend fun getSphere(id: Long): SphereResponseDto {
        val response = client.get("spheres/$id")

        return response.body()
    }

    suspend fun createSphere(request: SphereRequestDto): SphereResponseDto {
        val response = client.post("spheres") {
            setBody(request)
        }

        return response.body()
    }

    suspend fun updateSphere(
        id: Long,
        request: SphereRequestDto
    ): SphereResponseDto {
        val response = client.put("spheres/$id") {
            setBody(request)
        }

        return response.body()
    }

    suspend fun deleteSphere(id: Long) {
        client.delete("spheres/$id")
    }

    suspend fun getTasksBySphere(sphereId: Long): List<TaskResponseDto> {
        val response = client.get("spheres/$sphereId/tasks")

        return response.body()
    }

    /** Tasks **/
    suspend fun getTask(
        sphereId: Long,
        taskId: Long
    ): TaskResponseDto {
        val response = client.get("spheres/$sphereId/tasks/$taskId")

        return response.body()
    }

    suspend fun createTask(
        sphereId: Long,
        request: TaskRequestDto
    ): TaskResponseDto {
        val response = client.post("spheres/$sphereId/tasks") {
            setBody(request)
        }

        return response.body()
    }

    suspend fun updateTask(
        sphereId: Long,
        taskId: Long,
        request: TaskRequestDto
    ): TaskResponseDto {
        val response = client.put("spheres/$sphereId/tasks/$taskId") {
            setBody(request)
        }

        return response.body()
    }

    suspend fun deleteTask(
        sphereId: Long,
        taskId: Long
    ) {
        client.delete("spheres/$sphereId/tasks/$taskId")
    }

    /** Sessions **/
    suspend fun createSession(
        sphereId: Long,
        request: CreateSessionRequestDto
    ): SessionResponseDto {
        val response = client.post("spheres/$sphereId/sessions") {
            setBody(request)
        }

        return response.body<SessionResponseDto>()
    }

    suspend fun getSessionsBySphere(
        sphereId: Long
    ): List<SessionResponseDto> {
        val response = client.get("spheres/$sphereId/sessions")
        return response.body<List<SessionResponseDto>>()
    }

    suspend fun getSessionById(
        sphereId: Long,
        sessionId: Long
    ): SessionResponseDto {
        val response = client.get("spheres/$sphereId/sessions/$sessionId")
        return response.body<SessionResponseDto>()
    }

    suspend fun deleteSession(
        sphereId: Long,
        sessionId: Long
    ) {
        client.delete("spheres/$sphereId/sessions/$sessionId")
    }

    suspend fun getWeekSessions(
        sphereId: Long
    ): List<SessionResponseDto> {
        val response = client.get("spheres/$sphereId/sessions/week")
        return response.body<List<SessionResponseDto>>()
    }

    suspend fun startSession(
        sphereId: Long,
        sessionId: Long
    ): SessionResponseDto {
        val response = client.post("spheres/$sphereId/sessions/$sessionId/start")
        return response.body<SessionResponseDto>()
    }

    suspend fun pauseSession(
        sphereId: Long,
        sessionId: Long
    ): SessionResponseDto {
        val response = client.post("spheres/$sphereId/sessions/$sessionId/pause")
        return response.body<SessionResponseDto>()
    }

    suspend fun finishSession(
        sphereId: Long,
        sessionId: Long,
        request: FinishSessionRequestDto
    ): SessionResponseDto {
        val response = client.post("spheres/$sphereId/sessions/$sessionId/finish") {
            setBody(request)
        }

        return response.body<SessionResponseDto>()
    }

    suspend fun cancelSession(
        sphereId: Long,
        sessionId: Long
    ): SessionResponseDto {
        val response = client.post("spheres/$sphereId/sessions/$sessionId/cancel")
        return response.body<SessionResponseDto>()
    }

    /** Analytics **/
    suspend fun getWeeklyAnalytics(): WeeklyAnalyticsResponseDto {
        val response = client.get("analytics/weekly")
        return response.body()
    }
}