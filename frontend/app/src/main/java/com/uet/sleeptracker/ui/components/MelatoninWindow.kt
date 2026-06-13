package com.uet.sleeptracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MelatoninWindow(
    sizeN: Dp = 305.dp,
    heightM: Dp = 115.dp
) {
    val density = LocalDensity.current
    val sizeNPx = with(density) { sizeN.toPx() }
    val heightMPx = with(density) { heightM.toPx() }

    Box(
        modifier = Modifier
            .width(sizeN)
            .height(heightM)
            .clip(RoundedCornerShape(8.dp))
            .background(
                brush = Brush.radialGradient(
                    colorStops = arrayOf(
                        0.18f to Color(0xFF8150EA),
                        0.77f to Color(0xFF250A51)
                    ),
                    center = Offset(sizeNPx / 2f, heightMPx / 2f),
                    radius = sizeNPx / 2f
                )
            )
    )
}

@Preview
@Composable
fun MelatoninWindowPreview() {
    MelatoninWindow()
}