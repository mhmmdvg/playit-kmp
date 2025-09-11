package com.playit.models

import androidx.compose.ui.graphics.vector.ImageVector
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Music
import com.composables.icons.lucide.Search
import com.composables.icons.lucide.User
import com.playit.presentation.Screen

data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val screen: String,
)

val navigationItems = listOf(
    NavigationItem(
        title = "Music",
        icon = Lucide.Music,
        screen = Screen.HomeScreen.route
    ),
    NavigationItem(
        title = "Search",
        icon = Lucide.Search,
        screen = Screen.SearchScreen.route
    ),
    NavigationItem(
        title = "Profile",
        icon = Lucide.User,
        screen = Screen.ProfileScreen.route
    )
)
