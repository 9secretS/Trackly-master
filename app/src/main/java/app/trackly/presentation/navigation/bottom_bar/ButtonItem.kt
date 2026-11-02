package app.trackly.presentation.navigation.bottom_bar

import app.trackly.R

sealed class ButtonItem(
    val title: String,
    val iconId: Int,
    val selectedIcon: Int,
    val route: String
) {
    object Home: ButtonItem(
        "Home",
        R.drawable.home,
        R.drawable.home_selected,
        "HOME"
    )
    object Profile: ButtonItem(
        "Profile",
        R.drawable.profile,
        R.drawable.profile_selected,
        "PROFILE"
    )
    object AllTask: ButtonItem(
        "AllTask",
        R.drawable.anal,
        R.drawable.anal_selected,
        "ALL_TASKS"
    )
}