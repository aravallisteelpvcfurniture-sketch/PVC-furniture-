package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = AravalliGreenLight,
    onPrimary = Color.White,
    primaryContainer = AravalliGreenDark,
    onPrimaryContainer = Color.White,
    secondary = BadgeCarpenterCyan,
    background = Color(0xFF121814),
    surface = Color(0xFF1B231D),
    onBackground = Color(0xFFE2E8F0),
    onSurface = Color(0xFFE2E8F0)
)

private val LightColorScheme = lightColorScheme(
    primary = AravalliGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = AravalliGreenHeader,
    onPrimaryContainer = Color.White,
    secondary = BadgeConfirmOrderTeal,
    onSecondary = Color.White,
    tertiary = BadgeEstimateRed,
    background = AravalliBackground,
    surface = AravalliSurface,
    onBackground = AravalliTextPrimary,
    onSurface = AravalliTextPrimary,
    surfaceVariant = Color(0xFFE8F5E9),
    onSurfaceVariant = Color(0xFF2E7D32)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent branding colors matching Aravalli Furniture
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
