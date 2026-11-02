package app.trackly.presentation.screens.home

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import ir.ehsannarmani.compose_charts.models.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.trackly.R
import app.trackly.data.remote.dto.WeeklyAnalyticsResponseDto
import app.trackly.presentation.screens.sphereColorByKey
import app.trackly.presentation.ui.theme.GrayText
import app.trackly.presentation.ui.theme.Montserrat
import app.trackly.presentation.ui.theme.ShapeBg
import app.trackly.presentation.ui.theme.ShapeBorder
import app.trackly.presentation.ui.theme.Text
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.IndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.PopupProperties

@Composable
fun WeeklyReviewCard(
    analytics: WeeklyAnalyticsResponseDto?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = ShapeBg,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = ShapeBorder,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
            .height(170.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Review",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    color = Text,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                Image(
                    painter = painterResource(id = R.drawable.streak),
                    contentDescription = "Streak",
                    modifier = Modifier.size(18.dp)
                )

                Text(
                    text = " ${analytics?.streakDays ?: 0} day streak",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    color = Text,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            WeeklyChartLegend(analytics)

            Spacer(modifier = Modifier.height(8.dp))

            WeeklyLineChart(
                analytics = analytics,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(105.dp)
            )
        }
    }
}


@Composable
private fun WeeklyChartLegend(
    analytics: WeeklyAnalyticsResponseDto?
) {
    val lines = analytics?.weekChart.orEmpty().take(3)

    Row(
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        lines.forEach { line ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .background(
                            color = sphereColorByKey(line.sphereColor),
                            shape = RoundedCornerShape(50)
                        )
                )



                Text(
                    text = " ${line.sphereTitle}",
                    fontFamily = Montserrat,
                    color = Text,
                    fontSize = 12.sp
                )
            }
        }
    }
}


@Composable
private fun WeeklyLineChart(
    analytics: WeeklyAnalyticsResponseDto?,
    modifier: Modifier = Modifier
) {
    val weekDays = listOf(
        "MONDAY",
        "TUESDAY",
        "WEDNESDAY",
        "THURSDAY",
        "FRIDAY",
        "SATURDAY",
        "SUNDAY"
    )

    val chartLines = remember(analytics) {
        analytics
            ?.weekChart
            .orEmpty()
            .take(4)
            .map { chartLine ->
                val pointsByDay = chartLine.points.associateBy {
                    it.dayOfWeek.uppercase()
                }

                val values = weekDays.map { day ->
                    val seconds = pointsByDay[day]?.totalSeconds ?: 0
                    seconds / 3600.0
                }

                Line(
                    label = chartLine.sphereTitle,
                    values = values,
                    color = SolidColor(
                        sphereColorByKey(chartLine.sphereColor)
                    ),
                    firstGradientFillColor = sphereColorByKey(chartLine.sphereColor)
                        .copy(alpha = 0.18f),
                    secondGradientFillColor = androidx.compose.ui.graphics.Color.Transparent,
                    strokeAnimationSpec = tween(
                        durationMillis = 900,
                        easing = EaseInOutCubic
                    ),
                    gradientAnimationDelay = 300,
                    drawStyle = DrawStyle.Stroke(width = 1.dp)
                )
            }
    }

    if (chartLines.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No activity this week",
                fontFamily = Montserrat,
                color = GrayText,
                fontSize = 13.sp
            )
        }
    } else {
        LineChart(
            data = chartLines,
            modifier = modifier,
            indicatorProperties = HorizontalIndicatorProperties(enabled = false),
            popupProperties = PopupProperties(enabled = false),
            labelProperties = LabelProperties(enabled = false),
            labelHelperProperties = LabelHelperProperties(enabled = false)
        )
    }
}