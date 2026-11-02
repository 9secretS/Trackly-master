package app.trackly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import app.trackly.presentation.navigation.AuthRoute
import app.trackly.presentation.navigation.bottom_bar.BottomNavigationBar
import app.trackly.presentation.navigation.bottom_bar.ButtonItem
import app.trackly.presentation.screens.add_sphere.AddSphereRoute
import app.trackly.presentation.screens.add_sphere.AddSphereScreen
import app.trackly.presentation.screens.analytics.AllTasksScreen
import app.trackly.presentation.screens.auth.FirstEntranceScreen
import app.trackly.presentation.screens.auth.LoginScreen
import app.trackly.presentation.screens.auth.SignUpScreen
import app.trackly.presentation.screens.auth.SplashScreen
import app.trackly.presentation.screens.home.HomeScreen
import app.trackly.presentation.screens.profile.ProfileScreen
import app.trackly.presentation.screens.sessionTimer.SessionTimerRoute
import app.trackly.presentation.screens.sessionTimer.SessionTimerScreen
import app.trackly.presentation.screens.sphere_screen.SphereScreen
import app.trackly.presentation.screens.sphere_screen.SphereScreenRoute
import app.trackly.presentation.ui.theme.BackGr
import app.trackly.presentation.ui.theme.TracklyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(BackGr.toArgb()),
            navigationBarStyle = SystemBarStyle.dark(BackGr.toArgb())
        )
        setContent {
            TracklyTheme(darkTheme = true) {
                val navController = rememberNavController()

                val currentRoute = navController
                    .currentBackStackEntryAsState()
                    .value
                    ?.destination
                    ?.route

                val showBottomBar = currentRoute in listOf(
                    ButtonItem.Home.route,
                    ButtonItem.Profile.route,
                    ButtonItem.AllTask.route
                )

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            BottomNavigationBar(
                                currentRoute = currentRoute,
                                onNavigate = { route ->
                                    navController.navigate(route) {
                                        launchSingleTop = true
                                        restoreState = true
                                        popUpTo(ButtonItem.Home.route) {
                                            saveState = true
                                        }
                                    }
                                }
                            )
                        }
                    }
                ) { padding ->
                    NavHost(
                        navController = navController,
                        startDestination = AuthRoute.Splash.route,
                        modifier = Modifier
                            .padding(padding)
                    ) {
                        composable(AuthRoute.Splash.route) {
                            SplashScreen(
                                onAuthorized = {
                                    navController.navigate(ButtonItem.Home.route) {
                                        popUpTo(AuthRoute.Splash.route) {
                                            inclusive = true
                                        }
                                    }
                                },
                                onUnauthorized = {
                                    navController.navigate(AuthRoute.FirstEntrance.route) {
                                        popUpTo(AuthRoute.Splash.route) {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }

                        composable(AuthRoute.FirstEntrance.route) {
                            FirstEntranceScreen(
                                onLoginClick = {
                                    navController.navigate(AuthRoute.Login.route)
                                },
                                onSignUpClick = {
                                    navController.navigate(AuthRoute.SignUp.route)
                                }
                            )
                        }

                        composable(AuthRoute.Login.route) {
                            LoginScreen(
                                onLoginSuccess = {
                                    navController.navigate(ButtonItem.Home.route) {
                                        popUpTo(AuthRoute.FirstEntrance.route) {
                                            inclusive = true
                                        }
                                    }
                                },
                                onSignUpClick = {
                                    navController.navigate(AuthRoute.SignUp.route)
                                }
                            )
                        }

                        composable(AuthRoute.SignUp.route) {
                            SignUpScreen(
                                onSignUpSuccess = {
                                    navController.navigate(ButtonItem.Home.route) {
                                        popUpTo(AuthRoute.FirstEntrance.route) {
                                            inclusive = true
                                        }
                                    }
                                },
                                onLoginClick = {
                                    navController.navigate(AuthRoute.Login.route)
                                }
                            )
                        }

                        composable(ButtonItem.Home.route) { HomeScreen(navController) }
                        composable(ButtonItem.Profile.route) {
                            ProfileScreen(
                                onLogout = {
                                    navController.navigate(AuthRoute.FirstEntrance.route) {
                                        popUpTo(0) {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }

                        composable(AddSphereRoute.AddSphere.route) {
                            AddSphereScreen(
                                onClose = {
                                    navController.popBackStack()
                                },
                                onSaved = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable(ButtonItem.AllTask.route) { AllTasksScreen() }


                        composable(
                            route = SphereScreenRoute.Sphere.route,
                            arguments = listOf(
                                navArgument("id") { type = NavType.LongType },
                                navArgument("title") { type = NavType.StringType },
                                navArgument("colorKey") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            SphereScreen(
                                id = backStackEntry.arguments!!.getLong("id"),
                                title = backStackEntry.arguments!!.getString("title")!!,
                                colorKey = backStackEntry.arguments!!.getString("colorKey")!!,
                                navController = navController
                            )
                        }


                        composable(
                            route = SessionTimerRoute.Timer.route,
                            arguments = listOf(
                                navArgument("sphereId") { type = NavType.LongType },
                                navArgument("sessionId") { type = NavType.LongType },
                                navArgument("title") { type = NavType.StringType },
                                navArgument("planSeconds") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            SessionTimerScreen(
                                sphereId = backStackEntry.arguments!!.getLong("sphereId"),
                                sessionId = backStackEntry.arguments!!.getLong("sessionId"),
                                title = backStackEntry.arguments!!.getString("title")!!,
                                planSeconds = backStackEntry.arguments!!.getInt("planSeconds"),
                                onFinished = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }



            }
        }
    }
}
