package com.uet.sleeptracker.ui.screens.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class SleepCurveConfig(val distanceX: Float, val radius: Float)

private fun calculateCurveConfig(input: Float): SleepCurveConfig {
    val x = input.coerceIn(0f, 12f)
    val r1 = 64f / (8f * 0.7f) + 0.7f / 2f
    val r2 = 64f / (8f * 1.75f) + 1.75f / 2f
    val r3 = 64f / (8f * 2.5f) + 2.5f / 2f
    val distanceX: Float
    val radius: Float
    if (x < 4.8f) {
        val ratio = x / 4.8f
        distanceX = 8f + ratio * (5.5f - 8f)
        radius = r3 + ratio * (r2 - r3)
    } else {
        val ratio = (x - 4.8f) / (12f - 4.8f)
        distanceX = 5.5f + ratio * (2.1f - 5.5f)
        radius = r2 + ratio * (r1 - r2)
    }
    return SleepCurveConfig(
        distanceX = distanceX,
        radius = radius.coerceIn(minOf(r1, r2, r3), maxOf(r1, r2, r3))
    )
}

@Composable
internal fun SleepDebtHero(sleepDebtHours: Float) {
    val startValue = if (sleepDebtHours <= 4.8f) 4.8f else 12f
    val animatedDebt = remember(sleepDebtHours) { Animatable(startValue) }
    LaunchedEffect(sleepDebtHours) {
        animatedDebt.snapTo(startValue)
        animatedDebt.animateTo(
            targetValue = sleepDebtHours,
            animationSpec = tween(durationMillis = 1200)
        )
    }
    val currentDebt = animatedDebt.value

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(340.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF232429))
    ) {
        val density = LocalDensity.current
        val canvasWidthPx = constraints.maxWidth.toFloat()
        val rightEdgeInsetPx = 2f
        val drawableWidthPx = (canvasWidthPx - rightEdgeInsetPx).coerceAtLeast(1f)
        val vpW = 8f
        val scaleX = drawableWidthPx / vpW

        val config = calculateCurveConfig(currentDebt)
        val dxCanvasDp = with(density) { (config.distanceX * scaleX).toDp() }

        Canvas(modifier = Modifier.fillMaxSize()) {
            val vpH = 8f
            val scaleXc = (size.width - rightEdgeInsetPx).coerceAtLeast(1f) / vpW
            val scaleYc = size.height / vpH

            fun toCanvas(x: Float, y: Float) = Offset(
                x = x * scaleXc,
                y = (vpH / 2f - y) * scaleYc
            )

            data class C(val r: Float, val dx: Float)
            val circles = listOf(
                C(r = 64f / (8f * 0.7f) + 0.7f / 2f, dx = 2.1f),
                C(r = 64f / (8f * 1.75f) + 1.75f / 2f, dx = 5.5f),
                C(r = 64f / (8f * 2.5f) + 2.5f / 2f, dx = 8f)
            )

            val dynCenterX = config.distanceX - config.radius
            val dynCenter = toCanvas(dynCenterX, 0f)
            val dynRadiusPx = config.radius * scaleXc

            val dynCirclePath = Path().apply {
                addOval(androidx.compose.ui.geometry.Rect(center = dynCenter, radius = dynRadiusPx))
            }
            val fullCanvasPath = Path().apply {
                addRect(androidx.compose.ui.geometry.Rect(0f, 0f, size.width, size.height))
            }
            val leftPath = Path().apply {
                op(fullCanvasPath, dynCirclePath, PathOperation.Difference)
            }

            clipPath(leftPath) {
                drawRect(color = Color.Black)
                for (c in circles) {
                    val centerCanvas = toCanvas(c.dx - c.r, 0f)
                    val radiusPx = c.r * scaleXc
                    drawArc(
                        color = Color(0xFF232429),
                        startAngle = -90f, sweepAngle = 180f, useCenter = false,
                        topLeft = Offset(centerCanvas.x - radiusPx, centerCanvas.y - radiusPx),
                        size = Size(radiusPx * 2, radiusPx * 2),
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
            }

            clipPath(dynCirclePath) {
                drawRect(color = Color(0xFF232429))
                for (c in circles) {
                    val centerCanvas = toCanvas(c.dx - c.r, 0f)
                    val radiusPx = c.r * scaleXc
                    drawArc(
                        color = Color.Black,
                        startAngle = -90f, sweepAngle = 180f, useCenter = false,
                        topLeft = Offset(centerCanvas.x - radiusPx, centerCanvas.y - radiusPx),
                        size = Size(radiusPx * 2, radiusPx * 2),
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
            }

            val animDxCanvas = config.distanceX * scaleXc
            val animCircleRadius = (size.width - animDxCanvas) / 2f - 2.dp.toPx()
            val animCircleCenter = Offset(animDxCanvas + animCircleRadius + 2.dp.toPx(), size.height / 2f)
            drawCircle(color = Color(0xFF232429), radius = animCircleRadius, center = animCircleCenter)

            val purple = Color(0xFF7C5CFF)
            val dynBrush = if (currentDebt <= 4.8f) {
                Brush.verticalGradient(
                    0.0f to purple, 0.25f to purple,
                    0.4f to Color(0xFF2EABD0), 0.5f to Color(0xFF08F0B6),
                    0.6f to Color(0xFF2EABD0), 0.75f to purple, 1.0f to purple,
                    startY = dynCenter.y - dynRadiusPx, endY = dynCenter.y + dynRadiusPx
                )
            } else {
                Brush.verticalGradient(
                    0.0f to Color(0xFFB141BF), 0.25f to Color(0xFFB141BF),
                    0.4f to Color(0xFFCB676F), 0.5f to Color(0xFFFD9B03),
                    0.6f to Color(0xFFCB676F), 0.75f to Color(0xFFB141BF), 1.0f to Color(0xFFB141BF),
                    startY = dynCenter.y - dynRadiusPx, endY = dynCenter.y + dynRadiusPx
                )
            }

            drawArc(
                brush = dynBrush, startAngle = -90f, sweepAngle = 180f, useCenter = false,
                topLeft = Offset(dynCenter.x - dynRadiusPx, dynCenter.y - dynRadiusPx),
                size = Size(dynRadiusPx * 2, dynRadiusPx * 2),
                style = Stroke(width = 4.dp.toPx())
            )
        }

        // Text overlay
        val hoursText = "%.1f".format(sleepDebtHours)
        if (sleepDebtHours <= 7f) {
            // Left side: attached to the middle point (peak) of the dynamic line with a fixed distance of 16.dp
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .width((dxCanvasDp - 16.dp).coerceAtLeast(0.dp))
                    .fillMaxHeight(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Column(
                    modifier = Modifier.padding(start = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = hoursText,
                            color = Color.White,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 48.sp
                        )
                        Text(
                            text = " hrs",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    Text(
                        text = "Sleep debt",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        } else {
            // Right side: centered inside the gray circle
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(start = dxCanvasDp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = hoursText,
                        color = Color.White,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Normal,
                        lineHeight = 48.sp
                    )
                    Text(
                        text = " hrs",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                Text(
                    text = "Sleep debt",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}
