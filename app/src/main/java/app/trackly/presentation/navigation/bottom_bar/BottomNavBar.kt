package app.trackly.presentation.navigation.bottom_bar

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastCoerceIn
import androidx.navigation.NavController
import app.trackly.presentation.ui.theme.BNbg
import kotlinx.coroutines.selects.select

val shape = RoundedCornerShape(
    topStart = 32.dp,
    topEnd = 32.dp,
    bottomStart = 0.dp,
    bottomEnd = 0.dp
)
@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        ButtonItem.Profile,
        ButtonItem.Home,
        ButtonItem.AllTask
    )

    NavigationBar(
        containerColor = BNbg,
        modifier = Modifier
            .clip(shape)
    ) {
        items.forEach { item ->

            val selected = currentRoute == item.route

            val scale by animateFloatAsState(
                targetValue = if (selected) 1.25f else 1f,
                label = "iconScale"
            )

            val offsetY by animateDpAsState(
                targetValue = if (selected) -6.dp else 0.dp,
                label = "iconOffset"
            )

            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(
                        painter = painterResource(
                            id = if (selected) item.selectedIcon else item.iconId
                        ),
                        contentDescription = item.title,
                        modifier = Modifier
                            .size(26.dp)
                            .offset(y = offsetY)
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                            },
                        tint = Color.Unspecified
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}