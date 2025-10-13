package com.playit.models

import androidx.compose.ui.graphics.vector.ImageVector
import com.composables.icons.lucide.Library
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Music
import com.composables.icons.lucide.Search
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
        title = "Library",
        icon = Lucide.Library,
        screen = Screen.LibraryScreen.route
    )
)
