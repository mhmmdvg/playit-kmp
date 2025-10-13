package com.playit.presentation.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val LightColorScheme = lightColorScheme(
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFF2F2F7),
    onSurface = Color.Black,
    primary = Color.Black,
    onPrimary = Color.White,
    secondary = Color(0xFFE5E5EA),
    onSecondary = Color.Black,
)

val DarkColorScheme = darkColorScheme(
    background = Color(0xFF000000),
    surface = Color(0xFF1C1C1E),
    onSurface = Color.White,
    primary = Color.White,
    onPrimary = Color.Black,
    secondary = Color(0xFF2C2C2E),
    onSecondary = Color.White,
)