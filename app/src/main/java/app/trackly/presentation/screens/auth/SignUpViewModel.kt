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

data class SignUpUiState(
    val login: String = "",
    val email: String = "",
    val password: String = "",
    val loginError: Boolean = false,
    val emailError: Boolean = false,
    val passwordError: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRegistered: Boolean = false
)

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpUiState())
    val state = _state.asStateFlow()

    fun onLoginChange(value: String) {
        _state.value = _state.value.copy(
            login = value,
            loginError = false,
            errorMessage = null
        )
    }

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

    fun signUp() {
        val current = _state.value

        val loginError = current.login.isBlank()
        val emailError = current.email.isBlank()
        val passwordError = current.password.isBlank()

        if (loginError || emailError || passwordError) {
            _state.value = current.copy(
                loginError = loginError,
                emailError = emailError,
                passwordError = passwordError,
                errorMessage = "Fill in all fields"
            )
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                authRepository.register(
                    login = current.login.trim(),
                    email = current.email.trim(),
                    password = current.password
                )

                authRepository.login(
                    email = current.email.trim(),
                    password = current.password
                )

                _state.value = _state.value.copy(
                    isLoading = false,
                    isRegistered = true
                )
            } catch (e: ClientRequestException) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    emailError = true,
                    errorMessage = "Account already exists or data is invalid"
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Registration failed"
                )
            }
        }
    }
}