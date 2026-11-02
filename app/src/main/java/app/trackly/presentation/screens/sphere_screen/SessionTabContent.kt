package app.trackly.presentation.screens.sphere_screen

import android.widget.Toast
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.trackly.R
import app.trackly.domain.model.FocusSession
import app.trackly.domain.model.SessionStatus
import app.trackly.presentation.screens.sphere_screen.SessionsViewModel
import app.trackly.presentation.ui.theme.BackGr
import app.trackly.presentation.ui.theme.Border
import app.trackly.presentation.ui.theme.ButtonBg
import app.trackly.presentation.ui.theme.GrayText
import app.trackly.presentation.ui.theme.Montserrat
import app.trackly.presentation.ui.theme.Red
import app.trackly.presentation.ui.theme.ShapeBg
import app.trackly.presentation.ui.theme.ShapeBorder
import app.trackly.presentation.ui.theme.Text
import app.trackly.presentation.ui.theme.Yellow


@ExperimentalMaterial3Api
@Composable
fun SessionsTabContent(
    sphereId: Long,
    viewModel: SessionsViewModel,
    onOpenTimer: (
        sphereId: Long,
        sessionId: Long,
        title: String,
        planSeconds: Int
    ) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(sphereId) {
        viewModel.loadSessions(sphereId)
    }

    LaunchedEffect(Unit) {
        viewModel.message.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.openTimerEvent.collect { event ->
            onOpenTimer(
                event.sphereId,
                event.sessionId,
                event.title,
                event.planSeconds
            )
        }
    }

    Column {
        AddSessionButton(
            onClick = { showDialog = true }
        )

        Spacer(modifier = Modifier.height(16.dp))

        state.sessions.forEach { session ->
            val dismissState = rememberSwipeToDismissBoxState(
                positionalThreshold = { it * 0.75f },
                confirmValueChange = { value ->
                    if (value == SwipeToDismissBoxValue.EndToStart) {
                        viewModel.deleteSession(session)
                    }

                    false
                }
            )

            SwipeToDismissBox(
                state = dismissState,
                enableDismissFromStartToEnd = false,
                enableDismissFromEndToStart = true,
                backgroundContent = { SessionDeleteBackground() }
            ) {
                SessionItem(session = session)
            }
        }
    }

    if (showDialog) {
        AddSessionDialog(
            onDismiss = { showDialog = false },
            onStart = { title, seconds ->
                viewModel.createAndStartSession(
                    sphereId = sphereId,
                    title = title,
                    planSeconds = seconds
                )

                showDialog = false
            }
        )
    }
}

@Composable
private fun AddSessionButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .drawBehind {
                val strokeWidth = 2.dp.toPx()
                val dashWidth = 8.dp.toPx()
                val gapWidth = 8.dp.toPx()

                drawRoundRect(
                    color = ShapeBorder,
                    style = Stroke(
                        width = strokeWidth,
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(dashWidth, gapWidth)
                        )
                    ),
                    cornerRadius = CornerRadius(16.dp.toPx())
                )
            }
            .padding(16.dp)
            .fillMaxWidth()
            .height(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "+ Start new session",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Medium,
                color = Border,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun SessionItem(
    session: FocusSession
) {
    Box(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .background(
                color = ShapeBg,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = Border,
                shape = RoundedCornerShape(16.dp)
            )
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = session.title,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    color = Text,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = formatSeconds(
                        session.factualDurationSeconds
                            ?: session.planDurationSeconds
                    ),
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Medium,
                    color = BackGr,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .background(
                            color = if (session.status == SessionStatus.COMPLETED) Yellow else ButtonBg,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = when (session.status) {
                    SessionStatus.CREATED -> "Created"
                    SessionStatus.ACTIVE -> "Active"
                    SessionStatus.PAUSED -> "Paused"
                    SessionStatus.COMPLETED -> "Completed"
                    SessionStatus.CANCELED -> "Canceled"
                },
                fontFamily = Montserrat,
                color = GrayText,
                fontSize = 14.sp
            )

            if (!session.comment.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = session.comment,
                    fontFamily = Montserrat,
                    color = Text,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun SessionDeleteBackground() {
    Box(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .fillMaxSize()
            .background(
                color = Red,
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            painter = painterResource(R.drawable.delete),
            contentDescription = "delete",
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun AddSessionDialog(
    onDismiss: () -> Unit,
    onStart: (String, Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var minutes by remember { mutableFloatStateOf(25f) }

    val context = LocalContext.current

    AlertDialog(
        containerColor = ShapeBg,
        titleContentColor = Text,
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Start new session",
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
                color = Text,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column {
                Text(
                    text = "Enter the name:",
                    fontFamily = Montserrat,
                    color = Text,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                BasicTextField(
                    value = title,
                    onValueChange = {
                        if (it.length <= 50) title = it
                    },
                    singleLine = true,
                    textStyle = TextStyle(
                        color = Text,
                        fontFamily = Montserrat,
                        fontSize = 15.sp
                    ),
                    cursorBrush = SolidColor(Yellow),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = BackGr,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Border,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp),
                    decorationBox = { innerTextField ->
                        if (title.isBlank()) {
                            Text(
                                text = "Session name",
                                color = GrayText,
                                fontFamily = Montserrat,
                                fontSize = 15.sp
                            )
                        }

                        innerTextField()
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Time: ${minutes.toInt()} min",
                    fontFamily = Montserrat,
                    color = Text,
                    fontWeight = FontWeight.Medium
                )

                Slider(
                    value = minutes,
                    onValueChange = { minutes = it },
                    valueRange = 5f..180f,
                    steps = 34,
                    colors = SliderDefaults.colors(
                        thumbColor = Yellow,
                        activeTrackColor = Yellow,
                        inactiveTrackColor = ButtonBg
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isBlank()) {
                        Toast.makeText(
                            context,
                            "Enter session name",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        onStart(title.trim(), minutes.toInt() * 60)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Yellow,
                    contentColor = BackGr
                )
            ) {
                Text(
                    text = "Start",
                    fontFamily = Montserrat
                )
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonBg,
                    contentColor = Text
                )
            ) {
                Text(
                    text = "Cancel",
                    fontFamily = Montserrat
                )
            }
        }
    )
}

private fun formatSeconds(seconds: Int): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60

    return if (hours > 0) {
        "${hours}h :${minutes}min"
    } else {
        "${minutes}min"
    }
}

