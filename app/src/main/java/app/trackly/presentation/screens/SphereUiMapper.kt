package app.trackly.presentation.screens

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import app.trackly.R
import app.trackly.presentation.ui.theme.Blue
import app.trackly.presentation.ui.theme.Green
import app.trackly.presentation.ui.theme.Orange
import app.trackly.presentation.ui.theme.Red
import app.trackly.presentation.ui.theme.Yellow

fun sphereColorByKey(colorKey: String): Color {
    return when (colorKey.uppercase()) {
        "RED" -> Red
        "ORANGE" -> Orange
        "YELLOW" -> Yellow
        "GREEN" -> Green
        "BLUE" -> Blue
        "PURPLE" -> Color(0xFF9B43FF)
        else -> Yellow
    }
}

@DrawableRes
fun sphereIconByKey(iconKey: String): Int {
    return when (iconKey.uppercase()) {
        "FINANCE" -> R.drawable.finance_icon
        "GROWTH" -> R.drawable.growth_icon
        "STUDY" -> R.drawable.study_icon
        "HEALTH" -> R.drawable.health_icon
        "PAINT" -> R.drawable.piaint_icon
        "COMMUNITY" -> R.drawable.community_icon
        "HOME" -> R.drawable.home_icon
        "CARDIO" -> R.drawable.cardio_icon
        "SPORT" -> R.drawable.sport_icon
        "LAPTOP" -> R.drawable.laptop_icon
        "WRITE" -> R.drawable.write_icon
        "WORK" -> R.drawable.work_icon
        else -> R.drawable.study_icon
    }
}