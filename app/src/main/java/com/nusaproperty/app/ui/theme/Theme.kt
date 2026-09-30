package com.nusaproperty.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PrimaryNavy,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = SecondaryAmber,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    tertiary = TertiaryTeal,
    onTertiary = OnTertiary,
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,
    background = SurfaceCanvas,
    onBackground = OnSurface,
    surface = SurfaceCanvas,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    outline = OutlineColor,
    outlineVariant = OutlineVariant,
    error = StatusError,
    onError = OnPrimary,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryFixedDim,
    onPrimary = OnPrimaryFixed,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = PrimaryFixed,
    secondary = SecondaryFixedDim,
    onSecondary = OnSecondaryFixed,
    secondaryContainer = SecondaryBrown,
    onSecondaryContainer = SecondaryFixed,
    tertiary = TertiaryFixedDim,
    onTertiary = OnTertiaryFixed,
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = TertiaryFixed,
    background = PrimaryDark,
    onBackground = OnPrimary,
    surface = PrimaryDark,
    onSurface = OnPrimary,
    surfaceVariant = SurfaceContainer,
    onSurfaceVariant = OnPrimaryContainer,
    outline = OutlineVariant,
    outlineVariant = OutlineColor,
    error = StatusError,
    onError = OnPrimary
)

@Composable
fun NusaPropertyAndroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = SurfaceCard.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}