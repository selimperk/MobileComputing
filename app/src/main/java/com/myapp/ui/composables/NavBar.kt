package com.myapp.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.myapp.Route.*

@Composable
fun OurNavigationBar(onNavigate: (route: Route) -> Unit) {
    var currentRoute: Route by remember { mutableStateOf(HomeRoute) }
    val items = listOf(Home, Participant, Settings)

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (currentRoute == item.route) item.selectedIcon else item.unselected,
                        contentDescription = item.name
                    )
                },
                label = { Text(item.name) },
                selected = currentRoute == item.route,
                onClick = {
                    currentRoute = item.route
                    onNavigate(item.route)
                }
            )
        }
    }
}

sealed class BottomBarItem(
    val name: String,
    val route: Route,
    val selectedIcon: ImageVector,
    val unselected: ImageVector
)

data object Home :
    BottomBarItem("Home", HomeRoute, Icons.Filled.Home, Icons.Outlined.Home)

data object Participant :
    BottomBarItem("Teilnehmende", ParticipantRoute, Icons.Filled.Group, Icons.Outlined.Group)

data object Settings :
    BottomBarItem("Einstellungen", SettingsRoute, Icons.Filled.Settings, Icons.Outlined.Settings)


@Preview
@Composable
fun composable_preview() {
    OurNavigationBar {  }
}