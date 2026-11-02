package app.trackly.presentation.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.trackly.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: Boolean = false,
    val passwordError: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoggedIn: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state = _state.asStateFlow()

    fun onEmailChange(value: String) {
        _state.value = _state.value.copy(
            email = value,
            emailError = false,
            errorMessage = null
        )
    }

    fun onPasswordChange(value: String) {
        _state.value = _state.value.copy(
            password = value,
            passwordError = false,
            errorMessage = null
        )
    }

    fun login() {
        val current = _state.value

        val hasEmailError = current.email.isBlank()
        val hasPasswordError = current.password.isBlank()

        if (hasEmailError || hasPasswordError) {
            _state.value = current.copy(
                emailError = hasEmailError,
                passwordError = hasPasswordError,
                errorMessage = "Fill in email and password"
            )
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                authRepository.login(
                    email = current.email.trim(),
                    password = current.password
                )

                _state.value = _state.value.copy(
                    isLoading = false,
                    isLoggedIn = true
                )
            } catch (e: ClientRequestException) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    emailError = true,
                    passwordError = true,
                    errorMessage = "Wrong email or password"
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Login failed"
                )
            }
        }
    }
}