package app.trackly.presentation.screens.analytics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import app.trackly.R
import app.trackly.data.remote.dto.SessionPreviewDto
import app.trackly.data.remote.dto.SphereActivityItemDto
import app.trackly.data.remote.dto.WeeklyAnalyticsResponseDto
import app.trackly.presentation.screens.sphereColorByKey
import app.trackly.presentation.ui.theme.BackGr
import app.trackly.presentation.ui.theme.Blue
import app.trackly.presentation.ui.theme.Border
import app.trackly.presentation.ui.theme.GrayText
import app.trackly.presentation.ui.theme.Green
import app.trackly.presentation.ui.theme.Montserrat
import app.trackly.presentation.ui.theme.Orange
import app.trackly.presentation.ui.theme.Red
import app.trackly.presentation.ui.theme.ShapeBg
import app.trackly.presentation.ui.theme.ShapeBorder
import app.trackly.presentation.ui.theme.Text
import app.trackly.presentation.ui.theme.Yellow

@Composable
fun AllTasksScreen(
    viewModel: AllTasksScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        containerColor = BackGr
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            item {
                // LABEL
                Text(
                    text = stringResource(id = R.string.all_tasks_title),
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Text,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 32.dp)
                        .fillMaxWidth()
                )

                WeekSelector()

                Spacer(modifier = Modifier.height(28.dp))

                when {
                    state.isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Yellow)
                        }
                    }

                    state.error != null -> {
                        Text(
                            text = state.error ?: "",
                            color = Red,
                            fontFamily = Montserrat,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    else -> {
                        AnalyticsContent(
                            analytics = state.analytics
                        )
                    }
                }

                Spacer(modifier = Modifier.height(96.dp))
            }
        }
    }
}

@Composable
private fun AnalyticsContent(
    analytics: WeeklyAnalyticsResponseDto?
) {
    WeeklyActivityCard(analytics = analytics)

    Spacer(modifier = Modifier.height(28.dp))

    Row(
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        DayStatCard(
            title = "Best day",
            day = analytics?.bestDay?.dayOfWeek ?: "-",
            seconds = analytics?.bestDay?.totalSeconds ?: 0,
            backgroundColor = Green.copy(alpha = 0.16f),
            borderColor = Green.copy(alpha = 0.28f),
            modifier = Modifier.weight(1f)
        )

        DayStatCard(
            title = "Worst day",
            day = analytics?.worstDay?.dayOfWeek ?: "-",
            seconds = analytics?.worstDay?.totalSeconds ?: 0,
            backgroundColor = Red.copy(alpha = 0.16f),
            borderColor = Red.copy(alpha = 0.28f),
            modifier = Modifier.weight(1f)
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    MidTimeCard(
        averageDaySeconds = analytics?.averageDaySeconds ?: 0
    )

    Spacer(modifier = Modifier.height(32.dp))

    LongestSessionSection(
        session = analytics?.longestSession
    )
}

@Composable
private fun WeekSelector() {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "‹",
                color = Text,
                fontSize = 42.sp,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "Current week",
                color = Text,
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(3f)
            )

            Text(
                text = "›",
                color = Text,
                fontSize = 42.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Composable
private fun WeeklyActivityCard(
    analytics: WeeklyAnalyticsResponseDto?
) {
    val activity = analytics?.sphereActivity.orEmpty()
    val totalSeconds = activity.sumOf { it.totalSeconds }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = ShapeBg,
                shape = RoundedCornerShape(18.dp)
            )
            .border(
                width = 1.dp,
                color = ShapeBorder,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(18.dp)
    ) {
        Column {
            Text(
                text = "Weekly activity",
                color = Text,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                DonutActivityChart(
                    items = activity,
                    totalSeconds = totalSeconds,
                    modifier = Modifier.size(190.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Column(
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    activity.take(5).forEach { item ->
                        ActivityLegendItem(item = item)
                    }
                }
            }
        }
    }
}


@Composable
private fun DonutActivityChart(
    items: List<SphereActivityItemDto>,
    totalSeconds: Int,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 10.dp.toPx()
            val chartSize = Size(
                width = size.width - strokeWidth,
                height = size.height - strokeWidth
            )

            var startAngle = -90f

            if (items.isEmpty() || totalSeconds <= 0) {
                drawArc(
                    color = Border.copy(alpha = 0.35f),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = androidx.compose.ui.geometry.Offset(
                        strokeWidth / 2,
                        strokeWidth / 2
                    ),
                    size = chartSize,
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Butt
                    )
                )
            } else {
                items.forEach { item ->
                    val sweep = item.totalSeconds.toFloat() / totalSeconds.toFloat() * 360f

                    drawArc(
                        color = sphereColorByKey(item.sphereColor),
                        startAngle = startAngle,
                        sweepAngle = sweep,
                        useCenter = false,
                        topLeft = androidx.compose.ui.geometry.Offset(
                            strokeWidth / 2,
                            strokeWidth / 2
                        ),
                        size = chartSize,
                        style = Stroke(
                            width = strokeWidth,
                            cap = StrokeCap.Butt
                        )
                    )

                    startAngle += sweep
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = formatHours(totalSeconds),
                color = Text,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )
        }
    }
}


@Composable
private fun ActivityLegendItem(
    item: SphereActivityItemDto
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(0.88f)
    ) {
        Box(
            modifier = Modifier
                .size(5.dp)
                .background(
                    color = sphereColorByKey(item.sphereColor),
                    shape = RoundedCornerShape(50)
                )
        )

        Text(
            text = " ${shortenTitle(item.sphereTitle)}",
            color = Text,
            fontFamily = Montserrat,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = formatHours(item.totalSeconds),
            color = GrayText,
            fontFamily = Montserrat,
            fontSize = 13.sp
        )
    }
}


@Composable
private fun DayStatCard(
    title: String,
    day: String,
    seconds: Int,
    backgroundColor: androidx.compose.ui.graphics.Color,
    borderColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = title,
                color = Text,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatDayName(day),
                    color = Text,
                    fontFamily = Montserrat,
                    fontSize = 15.sp,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = formatHours(seconds),
                    color = Text,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
private fun MidTimeCard(
    averageDaySeconds: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Orange.copy(alpha = 0.16f),
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = Orange.copy(alpha = 0.28f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 18.dp, vertical = 18.dp)
    ) {
        Text(
            text = "Mid time",
            color = Text,
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "${formatHoursMinutes(averageDaySeconds)} / day",
            color = Text,
            fontFamily = Montserrat,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
    }
}


@Composable
private fun LongestSessionSection(
    session: SessionPreviewDto?
) {
    Text(
        text = "Longest session:",
        color = Text,
        fontFamily = Montserrat,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )

    Spacer(modifier = Modifier.height(14.dp))

    if (session == null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = ShapeBg,
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 1.dp,
                    color = Border,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(18.dp)
        ) {
            Text(
                text = "No completed sessions yet",
                color = GrayText,
                fontFamily = Montserrat,
                fontSize = 15.sp
            )
        }
    } else {
        LongestSessionCard(session = session)
    }
}

@Composable
private fun LongestSessionCard(
    session: SessionPreviewDto
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = ShapeBg,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = Border,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = session.title,
                    color = Text,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = formatHoursMinutes(session.factualDurationSeconds),
                    color = BackGr,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .background(
                            color = Yellow,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = session.comment ?: "No comment",
                color = GrayText,
                fontFamily = Montserrat,
                fontSize = 14.sp
            )
        }
    }
}


private fun formatHours(seconds: Int): String {
    val hours = seconds / 3600
    return "${hours} h"
}

private fun formatHoursMinutes(seconds: Int): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60

    return if (hours > 0) {
        "${hours} h ${minutes} min"
    } else {
        "${minutes} min"
    }
}

private fun formatDayName(day: String): String {
    return when (day.uppercase()) {
        "MONDAY" -> "Monday"
        "TUESDAY" -> "Tuesday"
        "WEDNESDAY" -> "Wednesday"
        "THURSDAY" -> "Thursday"
        "FRIDAY" -> "Friday"
        "SATURDAY" -> "Saturday"
        "SUNDAY" -> "Sunday"
        else -> day
    }
}

private fun shortenTitle(title: String): String {
    return if (title.length > 10) {
        title.take(9) + "..."
    } else {
        title
    }
}

