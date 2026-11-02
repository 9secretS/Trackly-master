package app.trackly.presentation.screens.add_sphere

sealed class AddSphereRoute(val route: String) {
    object AddSphere : AddSphereRoute("ADD_SPHERE")
}