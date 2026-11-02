package app.trackly.presentation.screens.sessionTimer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.trackly.data.remote.TracklyApi
import app.trackly.data.remote.dto.FinishSessionRequestDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionTimerViewModel @Inject constructor(
    private val api: TracklyApi
) : ViewModel() {

    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()

    private val _finishedEvent = MutableSharedFlow<Unit>()
    val finishedEvent = _finishedEvent.asSharedFlow()

    fun finishSession(
        sphereId: Long,
        sessionId: Long,
        comment: String?
    ) {
        viewModelScope.launch {
            try {
                api.finishSession(
                    sphereId = sphereId,
                    sessionId = sessionId,
                    request = FinishSessionRequestDto(comment)
                )

                _finishedEvent.emit(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                _message.emit("Failed to finish session")
            }
        }
    }
}