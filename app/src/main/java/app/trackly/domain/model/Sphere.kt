package app.trackly.domain.model

import app.trackly.presentation.ui.theme.Blue
import app.trackly.presentation.ui.theme.Green
import app.trackly.presentation.ui.theme.Orange
import app.trackly.presentation.ui.theme.Red
import app.trackly.presentation.ui.theme.Yellow

data class Sphere(
    val id: Long,
    val title: String,
    val colorKey: String,
    val iconKey: String,
    val hasTasks: Boolean
) {
    companion object {
        val primaryColors = mapOf(
            "RED" to Red,
            "ORANGE" to Orange,
            "YELLOW" to Yellow,
            "GREEN" to Green,
            "BLUE" to Blue
        )
    }
}