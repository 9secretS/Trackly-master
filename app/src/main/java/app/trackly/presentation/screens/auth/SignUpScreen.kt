package app.trackly.presentation.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import app.trackly.presentation.ui.theme.BackGr
import app.trackly.presentation.ui.theme.GrayText
import app.trackly.presentation.ui.theme.Montserrat
import app.trackly.presentation.ui.theme.PrimeWhite
import app.trackly.presentation.ui.theme.Red
import app.trackly.presentation.ui.theme.Text

@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit,
    onLoginClick: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isRegistered) {
        if (state.isRegistered) {
            onSignUpSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGr)
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(82.dp))

        AuthLogo(size = 150)

        Spacer(modifier = Modifier.height(38.dp))

        Text(
            text = "Sign Up:",
            color = Text,
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        AuthTextField(
            value = state.login,
            onValueChange = viewModel::onLoginChange,
            label = "Login",
            placeholder = "Enter your login",
            isError = state.loginError
        )

        Spacer(modifier = Modifier.height(14.dp))

        AuthTextField(
            value = state.email,
            onValueChange = viewModel::onEmailChange,
            label = "Email",
            placeholder = "Enter your email",
            isError = state.emailError
        )

        Spacer(modifier = Modifier.height(14.dp))

        AuthTextField(
            value = state.password,
            onValueChange = viewModel::onPasswordChange,
            label = "Password",
            placeholder = "Enter your password",
            isError = state.passwordError,
            visualTransformation = PasswordVisualTransformation()
        )

        if (state.errorMessage != null) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = state.errorMessage ?: "",
                color = Red,
                fontFamily = Montserrat,
                fontSize = 13.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        PrimaryAuthButton(
            text = if (state.isLoading) "Loading..." else "Create Account",
            onClick = viewModel::signUp,
            enabled = !state.isLoading
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.padding(bottom = 44.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Already have an account? ",
                color = GrayText,
                fontFamily = Montserrat,
                fontSize = 14.sp
            )

            Text(
                text = "Log In",
                color = PrimeWhite,
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onLoginClick()
                }
            )
        }
    }
}