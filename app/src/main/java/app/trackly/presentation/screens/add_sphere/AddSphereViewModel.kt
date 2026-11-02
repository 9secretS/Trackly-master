package app.trackly.presentation.screens.add_sphere

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.trackly.domain.model.Sphere
import app.trackly.domain.use_cases.sphere_use_cases.SphereUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddSphereUiState(
    val title: String = "",
    val selectedColorKey: String = "YELLOW",
    val selectedIconKey: String = "STUDY",
    val isLoading: Boolean = false,
    val titleError: Boolean = false
)

@HiltViewModel
class AddSphereViewModel @Inject constructor(
    private val sphereUseCases: SphereUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(AddSphereUiState())
    val state = _state.asStateFlow()

    private val _savedEvent = MutableSharedFlow<Unit>()
    val savedEvent = _savedEvent.asSharedFlow()

    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()

    fun onTitleChange(value: String) {
        if (value.length <= 50) {
            _state.value = _state.value.copy(
                title = value,
                titleError = false
            )
        }
    }

    fun onColorSelect(colorKey: String) {
        _state.value = _state.value.copy(
            selectedColorKey = colorKey
        )
    }

    fun onIconSelect(iconKey: String) {
        _state.value = _state.value.copy(
            selectedIconKey = iconKey
        )
    }

    fun saveSphere() {
        val current = _state.value

        if (current.title.isBlank()) {
            _state.value = current.copy(titleError = true)

            viewModelScope.launch {
                _message.emit("Enter the sphere name")
            }

            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            try {
                sphereUseCases.insertSphere(
                    Sphere(
                        id = 0L,
                        title = current.title.trim(),
                        colorKey = current.selectedColorKey,
                        iconKey = current.selectedIconKey,
                        hasTasks = true
                    )
                )

                _state.value = _state.value.copy(isLoading = false)
                _savedEvent.emit(Unit)
            } catch (e: Exception) {
                e.printStackTrace()

                _state.value = _state.value.copy(isLoading = false)
                _message.emit("Failed to create sphere")
            }
        }
    }
}