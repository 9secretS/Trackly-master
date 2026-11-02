package app.trackly.presentation.screens.sphere_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.trackly.data.remote.TracklyApi
import app.trackly.data.remote.dto.CreateSessionRequestDto
import app.trackly.data.remote.dto.toDomain
import app.trackly.domain.model.FocusSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SessionsUiState(
    val sessions: List<FocusSession> = emptyList(),
    val isLoading: Boolean = false
)

data class OpenTimerEvent(
    val sphereId: Long,
    val sessionId: Long,
    val title: String,
    val planSeconds: Int
)

@HiltViewModel
class SessionsViewModel @Inject constructor(
    private val api: TracklyApi
) : ViewModel() {

    private val _state = MutableStateFlow(SessionsUiState())
    val state = _state.asStateFlow()

    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()

    private val _openTimerEvent = MutableSharedFlow<OpenTimerEvent>()
    val openTimerEvent = _openTimerEvent.asSharedFlow()

    fun loadSessions(sphereId: Long) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            try {
                val sessions = api.getSessionsBySphere(sphereId)
                    .map { it.toDomain() }

                _state.value = _state.value.copy(
                    sessions = sessions,
                    isLoading = false
                )
            } catch (e: Exception) {
                e.printStackTrace()

                _state.value = _state.value.copy(isLoading = false)
                _message.emit("Failed to load sessions")
            }
        }
    }

    fun createAndStartSession(
        sphereId: Long,
        title: String,
        planSeconds: Int
    ) {
        viewModelScope.launch {
            try {
                val createdSession = api.createSession(
                    sphereId = sphereId,
                    request = CreateSessionRequestDto(
                        title = title,
                        planDurationSeconds = planSeconds
                    )
                )

                val startedSession = api.startSession(
                    sphereId = sphereId,
                    sessionId = createdSession.id
                )

                _openTimerEvent.emit(
                    OpenTimerEvent(
                        sphereId = sphereId,
                        sessionId = startedSession.id,
                        title = startedSession.title,
                        planSeconds = startedSession.planDurationSeconds
                    )
                )

                loadSessions(sphereId)
            } catch (e: Exception) {
                e.printStackTrace()
                _message.emit("Failed to start session")
            }
        }
    }

    fun deleteSession(session: FocusSession) {
        viewModelScope.launch {
            try {
                api.deleteSession(
                    sphereId = session.sphereId,
                    sessionId = session.id
                )

                _state.value = _state.value.copy(
                    sessions = _state.value.sessions.filterNot {
                        it.id == session.id
                    }
                )

                _message.emit("Session deleted")
            } catch (e: Exception) {
                e.printStackTrace()
                _message.emit("Failed to delete session")
            }
        }
    }
}