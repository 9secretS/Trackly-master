package app.trackly.presentation.screens.sessionTimer

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import app.trackly.presentation.screens.auth.PrimaryAuthButton
import app.trackly.presentation.ui.theme.BackGr
import app.trackly.presentation.ui.theme.GrayText
import app.trackly.presentation.ui.theme.Montserrat
import app.trackly.presentation.ui.theme.ShapeBg
import app.trackly.presentation.ui.theme.Text
import app.trackly.presentation.ui.theme.Yellow

@Composable
fun SessionTimerScreen(
    sphereId: Long,
    sessionId: Long,
    title: String,
    planSeconds: Int,
    onFinished: () -> Unit,
    viewModel: SessionTimerViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    var elapsedSeconds by remember { mutableIntStateOf(0) }
    var showFinishDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            elapsedSeconds += 1
        }
    }

    LaunchedEffect(Unit) {
        viewModel.message.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.finishedEvent.collect {
            onFinished()
        }
    }

    val progress = if (planSeconds <= 0) {
        0f
    } else {
        (elapsedSeconds.toFloat() / planSeconds.toFloat()).coerceIn(0f, 1f)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGr)
            .padding(horizontal = 28.dp, vertical = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title.uppercase(),
            color = Text,
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )

        Spacer(modifier = Modifier.height(140.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp),
            color = Yellow,
            trackColor = ShapeBg
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "${formatTimer(elapsedSeconds)} / ${formatTimer(planSeconds)}",
            color = Text,
            fontFamily = Montserrat,
            fontSize = 26.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        PrimaryAuthButton(
            text = "End session",
            onClick = {
                showFinishDialog = true
            }
        )
    }

    if (showFinishDialog) {
        FinishSessionDialog(
            elapsedSeconds = elapsedSeconds,
            onDismiss = { showFinishDialog = false },
            onFinish = { comment ->
                viewModel.finishSession(
                    sphereId = sphereId,
                    sessionId = sessionId,
                    comment = comment
                )
                showFinishDialog = false
            }
        )
    }
}

@Composable
private fun FinishSessionDialog(
    elapsedSeconds: Int,
    onDismiss: () -> Unit,
    onFinish: (String?) -> Unit
) {
    var comment by remember { mutableStateOf("") }

    AlertDialog(
        containerColor = ShapeBg,
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "End session",
                color = Text,
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Column {
                Text(
                    text = "Time ${formatTimer(elapsedSeconds)}",
                    color = Text,
                    fontFamily = Montserrat,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                BasicTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    textStyle = TextStyle(
                        color = Text,
                        fontFamily = Montserrat,
                        fontSize = 14.sp
                    ),
                    cursorBrush = SolidColor(Yellow),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(
                            color = BackGr,
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp),
                    decorationBox = { innerTextField ->
                        if (comment.isBlank()) {
                            Text(
                                text = "Comment",
                                color = GrayText,
                                fontFamily = Montserrat,
                                fontSize = 14.sp
                            )
                        }

                        innerTextField()
                    }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onFinish(comment.takeIf { it.isNotBlank() })
                }
            ) {
                Text(
                    text = "End",
                    color = Yellow,
                    fontFamily = Montserrat
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    color = Text,
                    fontFamily = Montserrat
                )
            }
        }
    )
}

private fun formatTimer(seconds: Int): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60

    return "%02d:%02d:%02d".format(hours, minutes, secs)
}
