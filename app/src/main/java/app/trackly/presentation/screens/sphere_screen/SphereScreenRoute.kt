package app.trackly.presentation.screens.sphere_screen

import android.net.Uri

sealed class SphereScreenRoute(val route: String) {
    object Sphere : SphereScreenRoute("sphere/{id}/{title}/{colorKey}") {
        fun createRoute(
            id: Long,
            title: String,
            colorKey: String
        ): String {
            return "sphere/$id/${Uri.encode(title)}/${Uri.encode(colorKey)}"
        }
    }
}
