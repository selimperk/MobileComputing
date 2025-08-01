package com.myapp.datastore.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat



// 🎨 Farben aus deinem gewünschten Design
private val DarkColorScheme = darkColorScheme(
    primary = HeaderOrange,
    secondary = ButtonBlue,
    tertiary = ActiveTogglePurple,
    background = BackgroundBlue,
    onPrimary = TextWhite,
    onSecondary = TextWhite,
    onBackground = TextWhite,
    surface = BackgroundBlue,
    onSurface = TextWhite
)

private val LightColorScheme = lightColorScheme(
    primary = HeaderOrange,
    secondary = ButtonBlue,
    tertiary = ActiveTogglePurple,
    background = BackgroundBlue,
    onPrimary = TextWhite,
    onSecondary = TextWhite,
    onBackground = TextWhite,
    surface = BackgroundBlue,
    onSurface = TextWhite
)

@Composable
fun PersistenceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Deaktiviert, weil du custom Farben nutzt
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
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
