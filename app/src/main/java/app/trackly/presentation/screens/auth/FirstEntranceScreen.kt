package app.trackly.presentation.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.trackly.presentation.ui.theme.BackGr
import app.trackly.presentation.ui.theme.GrayText
import app.trackly.presentation.ui.theme.Montserrat
import app.trackly.presentation.ui.theme.Text

@Composable
fun FirstEntranceScreen(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGr)
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(90.dp))

        AuthLogo(size = 260)

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Welcome to\nTrackly!",
            color = Text,
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            lineHeight = 36.sp
        )

        Spacer(modifier = Modifier.height(80.dp))

        Text(
            text = "Track your time spent working on different areas with convenient charts and analysis",
            color = GrayText,
            fontFamily = Montserrat,
            fontSize = 17.sp,
            textAlign = TextAlign.Center,
            lineHeight = 23.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        PrimaryAuthButton(
            text = "Log In",
            onClick = onLoginClick
        )

        Spacer(modifier = Modifier.height(20.dp))

        SecondaryAuthButton(
            text = "Sign Up",
            onClick = onSignUpClick
        )

        Spacer(modifier = Modifier.height(44.dp))
    }
}
