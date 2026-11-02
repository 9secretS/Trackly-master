package app.trackly.presentation.screens.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.trackly.data.remote.TracklyApi
import app.trackly.data.remote.dto.WeeklyAnalyticsResponseDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AnalyticsUiState(
    val analytics: WeeklyAnalyticsResponseDto? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AllTasksScreenViewModel @Inject constructor(
    private val api: TracklyApi
) : ViewModel() {

    private val _state = MutableStateFlow(AnalyticsUiState())
    val state = _state.asStateFlow()

    init {
        loadAnalytics()
    }

    fun loadAnalytics() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                error = null
            )

            try {
                val analytics = api.getWeeklyAnalytics()

                _state.value = _state.value.copy(
                    analytics = analytics,
                    isLoading = false
                )
            } catch (e: Exception) {
                e.printStackTrace()

                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Failed to load analytics"
                )
            }
        }
    }
}