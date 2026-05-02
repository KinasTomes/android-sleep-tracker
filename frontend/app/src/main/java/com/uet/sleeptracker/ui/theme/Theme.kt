package com.uet.sleeptracker.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = SleepPrimary,
    secondary = SleepSecondary,
    tertiary = SleepTertiary,
    background = SleepBackground,
    surface = SleepSurface,
    surfaceVariant = SleepSurfaceSelected,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = SleepOnBackground,
    onSurface = SleepOnSurface,
    onSurfaceVariant = SleepOnSurfaceVariant,
    outline = SleepOutlineLight
)

@Composable
fun SleeptrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else DarkColorScheme // Force dark for sleep tracker feel

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
