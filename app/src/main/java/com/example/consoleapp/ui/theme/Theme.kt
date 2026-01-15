package com.example.consoleapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkScheme = darkColorScheme(
    background = BgDark,
    surface = SurfaceDark,
    surfaceContainer = SurfaceDark,
    surfaceContainerLow = SurfaceDark2,
    surfaceVariant = SurfaceDark2,

    primary = AccentBlue,
    tertiary = AccentGreen,

    onBackground = TextHigh,
    onSurface = TextHigh,
    onSurfaceVariant = TextMid,

    error = Color(0xFFFF6B6B)
)

@Composable
fun ConsoleAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkScheme,
        typography = Typography,
        content = content
    )
}
