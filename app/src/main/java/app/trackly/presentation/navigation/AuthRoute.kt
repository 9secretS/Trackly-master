package app.trackly.presentation.navigation

sealed class AuthRoute(val route: String) {
    object Splash : AuthRoute("SPLASH")
    object FirstEntrance : AuthRoute("FIRST_ENTRANCE")
    object Login : AuthRoute("LOGIN")
    object SignUp : AuthRoute("SIGN_UP")
}