package com.example.pomodoro.ui.theme

import android.app.Activity
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

object Theme {
    var currentThemeType: ThemeType = ThemeType.STANDARD
    val specialColor
        get() = when (currentThemeType) {
            ThemeType.STANDARD -> AshGrayGreen
            else -> currentThemeType.backgroundColor
        }

    val darkColorScheme = darkColorScheme(
        primary = WhiteCatskill,
        secondary = GrayDarkShark,
        secondaryContainer = GrayDarkShark,
        tertiary = GrayOslo,
        background = BlueDarkWoodsmoke,
        surface = BlueDarkBunker,
        onPrimary = GrayAbbey,
        onSecondary = GrayRollingStone,
        onTertiary = GrayIron,
        onBackground = GrayAthens,
        onSurface = GrayAthens
    )

    val lightColorScheme: ColorScheme
        get() = lightColorScheme(
            primary = GrayAbbey,
            secondary = GrayRollingStone,
            secondaryContainer = AlphaGrayRollingStone,
            tertiary = GrayIron,
            background = currentThemeType.backgroundColor,
            surface = GrayAthens,
            onPrimary = WhiteCatskill,
            onSecondary = GrayDarkShark,
            onTertiary = GrayOslo,
            onBackground = BlueDarkWoodsmoke,
            onSurface = BlueDarkBunker
        )

    enum class ThemeType(val backgroundColor: Color) {
        STANDARD(GrayAthens),
        BLUE(LightBlue),
        GREEN(AshGrayGreen),
        PINK(OrchidPink),
        BROWN(DesertSandBrown),
        VIOLET(PeriwinkleViolet)
    }
}

@Composable
fun PomodoroTheme(
    darkTheme: Boolean,
    themeType: Theme.ThemeType,
    content: @Composable () -> Unit
) {
    Theme.currentThemeType = themeType
    val view = LocalView.current
    val colorScheme = if (darkTheme) Theme.darkColorScheme else Theme.lightColorScheme

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}