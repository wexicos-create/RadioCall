package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val EditorialDarkColorScheme = darkColorScheme(
    primary = RadioAmber,
    onPrimary = DarkBackground,
    primaryContainer = RadioAmberDim,
    onPrimaryContainer = TextPrimary,
    secondary = SignalGreen,
    onSecondary = DarkBackground,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    background = DarkBackground,
    onBackground = TextPrimary,
    outline = BorderColor
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Forced dark mode for AFM Secure editorial look
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = EditorialDarkColorScheme,
        typography = Typography,
        content = content
    )
}
