package com.example.myapp.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.serialization.Serializable

@Composable
@Preview
fun OurNavigationBar(onNavigate: (route: Route) -> Unit = {}) {
    var currentRoute: Route by remember { mutableStateOf(HomeRoute) }
    val items = listOf(Home, Add, Settings)

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        if (currentRoute == item.route) item.selectedIcon else item.unselected,
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

// ---------------------
// Route & BottomBarItem
// ---------------------

sealed class BottomBarItem(
    val name: String,
    val route: Route,
    val selectedIcon: ImageVector,
    val unselected: ImageVector
)

data object Home :
    BottomBarItem("Home", HomeRoute, Icons.Filled.Home, Icons.Outlined.Home)

data object Add :
    BottomBarItem("Add", AddRoute, Icons.Filled.Add, Icons.Outlined.Add)

data object Settings :
    BottomBarItem("Settings", SettingsRoute, Icons.Filled.Settings, Icons.Outlined.Settings)


// Diese drei Objekte brauchst du auch in deiner MainActivity für die Navigation
sealed class Route

@Serializable
data object HomeRoute : Route()

@Serializable
data object AddRoute : Route()

@Serializable
data object SettingsRoute : Route()
