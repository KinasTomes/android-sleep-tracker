package com.uet.sleeptracker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.uet.sleeptracker.R

// Define the HaleNoir custom FontFamily
val HaleNoirFontFamily = FontFamily(
    Font(R.font.halenoir_compact_regular, FontWeight.Normal),
    Font(R.font.halenoir_compact_medium, FontWeight.Medium),
    Font(R.font.halenoir_compact_demibold, FontWeight.SemiBold),
    Font(R.font.halenoir_compact_demibold, FontWeight.Bold)
)

val NoyhGeometricSlimFontFamily = FontFamily(
    Font(R.font.noyh_geometric_slim_regular, FontWeight.Normal),
    Font(R.font.noyh_geometric_slim_medium, FontWeight.SemiBold),
    Font(R.font.noyh_geometric_slim_semi_light, FontWeight.Light)
)

// Inter FontFamily with full weight support
val InterFontFamily = FontFamily(
    Font(R.font.inter_thin, FontWeight.Thin),
    Font(R.font.inter_extra_light, FontWeight.ExtraLight),
    Font(R.font.inter_light, FontWeight.Light),
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semi_bold, FontWeight.SemiBold),
    Font(R.font.inter_bold, FontWeight.Bold),
    Font(R.font.inter_extra_bold, FontWeight.ExtraBold),
    Font(R.font.inter_black, FontWeight.Black)
)

// Set of Material typography styles using Inter FontFamily
private val defaultTypography = Typography()
val Typography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = InterFontFamily),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = InterFontFamily),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = InterFontFamily),
    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = InterFontFamily),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = InterFontFamily),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = InterFontFamily),
    titleLarge = defaultTypography.titleLarge.copy(fontFamily = InterFontFamily),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = InterFontFamily),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = InterFontFamily),
    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = InterFontFamily),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = InterFontFamily),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = InterFontFamily),
    labelLarge = defaultTypography.labelLarge.copy(fontFamily = InterFontFamily),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = InterFontFamily),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = InterFontFamily)
)