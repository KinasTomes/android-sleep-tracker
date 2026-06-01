package com.uet.sleeptracker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.uet.sleeptracker.R

// Define the HaleNoir custom FontFamily
val HaleNoirFontFamily = FontFamily(
    Font(R.font.halenoir_compact_regular, FontWeight.Normal),
    Font(R.font.halenoir_compact_demibold, FontWeight.SemiBold),
    Font(R.font.halenoir_compact_demibold, FontWeight.Bold)
)

// Set of Material typography styles using the custom FontFamily
private val defaultTypography = Typography()
val Typography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = HaleNoirFontFamily),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = HaleNoirFontFamily),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = HaleNoirFontFamily),
    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = HaleNoirFontFamily),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = HaleNoirFontFamily),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = HaleNoirFontFamily),
    titleLarge = defaultTypography.titleLarge.copy(fontFamily = HaleNoirFontFamily),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = HaleNoirFontFamily),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = HaleNoirFontFamily),
    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = HaleNoirFontFamily),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = HaleNoirFontFamily),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = HaleNoirFontFamily),
    labelLarge = defaultTypography.labelLarge.copy(fontFamily = HaleNoirFontFamily),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = HaleNoirFontFamily),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = HaleNoirFontFamily)
)