package app.trackly.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.trackly.data.remote.TracklyApi
import app.trackly.data.remote.dto.WeeklyAnalyticsResponseDto
import app.trackly.domain.model.Sphere
import app.trackly.domain.use_cases.sphere_use_cases.SphereUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val weeklyAnalytics: WeeklyAnalyticsResponseDto? = null,
    val isAnalyticsLoading: Boolean = false,
    val analyticsError: String? = null
)

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val sphereUseCases: SphereUseCases,
    private val api: TracklyApi
) : ViewModel() {

    val spheresList: Flow<List<Sphere>> = sphereUseCases.getAllSpheres()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()

    init {
        loadWeeklyAnalytics()
    }

    fun loadWeeklyAnalytics() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isAnalyticsLoading = true,
                analyticsError = null
            )

            try {
                val analytics = api.getWeeklyAnalytics()

                _uiState.value = _uiState.value.copy(
                    weeklyAnalytics = analytics,
                    isAnalyticsLoading = false
                )
            } catch (e: Exception) {
                e.printStackTrace()

                _uiState.value = _uiState.value.copy(
                    isAnalyticsLoading = false,
                    analyticsError = "Failed to load analytics"
                )
            }
        }
    }

    fun addSphere(
        title: String,
        colorKey: String,
        iconKey: String
    ) {
        viewModelScope.launch {
            try {
                sphereUseCases.insertSphere(
                    Sphere(
                        id = 0L,
                        title = title,
                        colorKey = colorKey,
                        iconKey = iconKey,
                        hasTasks = true
                    )
                )

                loadWeeklyAnalytics()
            } catch (e: Exception) {
                _message.emit("Failed to create sphere")
            }
        }
    }

    fun deleteSphere(sphere: Sphere) {
        viewModelScope.launch {
            try {
                sphereUseCases.deleteSphere(sphere)
                loadWeeklyAnalytics()
                _message.emit("Sphere deleted")
            } catch (e: Exception) {
                _message.emit("Failed to delete sphere")
            }
        }
    }
}