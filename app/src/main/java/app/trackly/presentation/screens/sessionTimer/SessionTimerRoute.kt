package app.trackly.presentation.screens.sessionTimer

import android.net.Uri

sealed class SessionTimerRoute(val route: String) {
    object Timer : SessionTimerRoute(
        "session_timer/{sphereId}/{sessionId}/{title}/{planSeconds}"
    ) {
        fun createRoute(
            sphereId: Long,
            sessionId: Long,
            title: String,
            planSeconds: Int
        ): String {
            return "session_timer/$sphereId/$sessionId/${Uri.encode(title)}/$planSeconds"
        }
    }
}