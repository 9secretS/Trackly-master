package app.trackly.presentation.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.trackly.R
import app.trackly.presentation.ui.theme.AuthText
import app.trackly.presentation.ui.theme.BackGr
import app.trackly.presentation.ui.theme.ErrorBg
import app.trackly.presentation.ui.theme.ErrorRed
import app.trackly.presentation.ui.theme.GrayText
import app.trackly.presentation.ui.theme.Montserrat
import app.trackly.presentation.ui.theme.PrimeWhite
import app.trackly.presentation.ui.theme.Red
import app.trackly.presentation.ui.theme.Text
import app.trackly.presentation.ui.theme.Yellow

@Composable
fun PrimaryAuthButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Yellow,
            contentColor = BackGr
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Text(
            text = text,
            fontFamily = Montserrat,
            fontWeight = FontWeight.Medium
        )
    }
}


@Composable
fun SecondaryAuthButton(
    text: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 1.dp,
            color = PrimeWhite
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = PrimeWhite
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Text(
            text = text,
            fontFamily = Montserrat,
            fontWeight = FontWeight.Medium
        )
    }
}


@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    var isFocused by remember { mutableStateOf(false) }

    val borderColor = when {
        isError -> ErrorRed
        isFocused -> Yellow
        else -> AuthText
    }

    val labelColor = when {
        isError -> ErrorRed
        else -> AuthText
    }

    val backgroundColor = when {
        isError -> ErrorBg
        isFocused -> PrimeWhite.copy(0.10F)
        else -> PrimeWhite.copy(0.05F)
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            color = labelColor,
            fontFamily = Montserrat,
            fontSize = 13.sp,
            modifier = Modifier.padding(start = 8.dp, bottom = 6.dp)
        )

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(
                color = Text,
                fontFamily = Montserrat,
                fontSize = 15.sp
            ),
            cursorBrush = SolidColor(Yellow),
            visualTransformation = visualTransformation,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(10.dp)
                )
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .onFocusChanged {
                    isFocused = it.isFocused
                },
            decorationBox = { innerTextField ->
                if (value.isBlank()) {
                    Text(
                        text = placeholder,
                        color = AuthText.copy(0.5f),
                        fontFamily = Montserrat,
                        fontSize = 15.sp
                    )
                }

                innerTextField()
            }
        )
    }
}


@Composable
fun AuthLogo(
    modifier: Modifier = Modifier,
    size: Int = 180
) {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "Trackly logo",
        modifier = modifier.size(size.dp)
    )
}


@Composable
@Preview
fun Prev() {
    AuthTextField(
        value = "",
        onValueChange = { "sdfsd" },
        label = "",
        placeholder = "",
        modifier = Modifier,
    )
}
