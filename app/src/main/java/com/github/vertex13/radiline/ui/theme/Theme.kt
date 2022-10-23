package com.github.vertex13.radiline.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat

private val LightColorScheme = lightColorScheme(
    primary = PaletteTokens.Primary40,
    onPrimary = PaletteTokens.Primary100,
    primaryContainer = PaletteTokens.Primary90,
    onPrimaryContainer = PaletteTokens.Primary10,
    inversePrimary = PaletteTokens.Primary80,

    secondary = PaletteTokens.Secondary40,
    onSecondary = PaletteTokens.Secondary100,
    secondaryContainer = PaletteTokens.Secondary90,
    onSecondaryContainer = PaletteTokens.Secondary10,

    tertiary = PaletteTokens.Tertiary40,
    onTertiary = PaletteTokens.Tertiary100,
    tertiaryContainer = PaletteTokens.Tertiary90,
    onTertiaryContainer = PaletteTokens.Tertiary10,

    background = PaletteTokens.Neutral99,
    onBackground = PaletteTokens.Neutral10,
    surface = PaletteTokens.Neutral99,
    onSurface = PaletteTokens.Neutral10,
)

private val DarkColorScheme = darkColorScheme(
    primary = PaletteTokens.Primary80,
    onPrimary = PaletteTokens.Primary20,
    primaryContainer = PaletteTokens.Primary30,
    onPrimaryContainer = PaletteTokens.Primary90,
    inversePrimary = PaletteTokens.Primary40,

    secondary = PaletteTokens.Secondary80,
    onSecondary = PaletteTokens.Secondary20,
    secondaryContainer = PaletteTokens.Secondary30,
    onSecondaryContainer = PaletteTokens.Secondary90,

    tertiary = PaletteTokens.Tertiary80,
    onTertiary = PaletteTokens.Tertiary20,
    tertiaryContainer = PaletteTokens.Tertiary30,
    onTertiaryContainer = PaletteTokens.Tertiary90,

    background = PaletteTokens.Neutral10,
    onBackground = PaletteTokens.Neutral90,
    surface = PaletteTokens.Neutral10,
    onSurface = PaletteTokens.Neutral90,
)

@Composable
fun RadilineTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
