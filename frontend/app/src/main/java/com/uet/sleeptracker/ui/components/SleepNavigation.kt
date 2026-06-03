package com.uet.sleeptracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.ui.theme.SleepPrimary

enum class SleepBottomNavTab {
    MySleep,
    Energy,
    Tools,
    Stats,
    Learn
}

@Composable
fun SleepBottomNavigation(
    selectedTab: SleepBottomNavTab,
    onTabSelected: (SleepBottomNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(0xFF0F0F11),
        modifier = modifier
            .fillMaxWidth()
            .height(76.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SleepBottomNavTab.entries.forEach { tab ->
                SleepBottomNavigationItem(
                    tab = tab,
                    isSelected = tab == selectedTab,
                    onClick = { onTabSelected(tab) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun SleepBottomNavigationItem(
    tab: SleepBottomNavTab,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = if (isSelected) Color.White else Color(0xFF7C5CFF)
    val textColor = if (isSelected) Color.White else Color(0xFF8E8E93)
    val label = when (tab) {
        SleepBottomNavTab.MySleep -> "Sleep"
        SleepBottomNavTab.Energy -> "Energy"
        SleepBottomNavTab.Tools -> "Tools"
        SleepBottomNavTab.Stats -> "Progress"
        SleepBottomNavTab.Learn -> "Learn"
    }

    val iconType = when (tab) {
        SleepBottomNavTab.MySleep -> SleepIconType.Target
        SleepBottomNavTab.Energy -> SleepIconType.Lines
        SleepBottomNavTab.Tools -> SleepIconType.Briefcase
        SleepBottomNavTab.Stats -> SleepIconType.LineChart
        SleepBottomNavTab.Learn -> SleepIconType.Lightbulb
    }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SleepIconPlaceholder(
            color = color,
            size = 24.dp,
            type = iconType
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
fun SleepTopActionIcon(
    type: SleepIconType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = SleepPrimary,
    size: Dp = 30.dp
) {
    IconButton(onClick = onClick, modifier = modifier) {
        SleepIconPlaceholder(
            color = tint,
            size = size,
            type = type
        )
    }
}

@Composable
fun SleepIconPlaceholder(
    color: Color,
    size: Dp,
    type: SleepIconType,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 2.dp.toPx()
            val w = size.toPx()
            val h = size.toPx()

            when (type) {
                SleepIconType.Bell -> {
                    // Draw bell outline
                    drawArc(
                        color = color,
                        startAngle = 180f,
                        sweepAngle = 180f,
                        useCenter = false,
                        topLeft = Offset(w * 0.2f, h * 0.2f),
                        size = Size(w * 0.6f, h * 0.6f),
                        style = Stroke(width = strokeWidth)
                    )
                    drawLine(
                        color = color,
                        start = Offset(w * 0.15f, h * 0.7f),
                        end = Offset(w * 0.85f, h * 0.7f),
                        strokeWidth = strokeWidth
                    )
                    drawCircle(
                        color = color,
                        radius = w * 0.08f,
                        center = Offset(w * 0.5f, h * 0.82f)
                    )
                }

                SleepIconType.Profile -> {
                    // Draw profile outline
                    drawCircle(
                        color = color,
                        radius = w * 0.44f,
                        center = Offset(w * 0.5f, h * 0.5f),
                        style = Stroke(width = strokeWidth)
                    )
                    drawCircle(
                        color = color,
                        radius = w * 0.18f,
                        center = Offset(w * 0.5f, h * 0.38f),
                        style = Stroke(width = strokeWidth)
                    )
                    drawArc(
                        color = color,
                        startAngle = 180f,
                        sweepAngle = 180f,
                        useCenter = false,
                        topLeft = Offset(w * 0.2f, h * 0.54f),
                        size = Size(w * 0.6f, h * 0.5f),
                        style = Stroke(width = strokeWidth)
                    )
                }

                SleepIconType.Target -> {
                    // Target concentric circles
                    drawCircle(
                        color = color,
                        radius = w * 0.16f,
                        center = Offset(w * 0.5f, h * 0.5f)
                    )
                    drawCircle(
                        color = color,
                        radius = w * 0.4f,
                        center = Offset(w * 0.5f, h * 0.5f),
                        style = Stroke(width = strokeWidth)
                    )
                }

                SleepIconType.Lines -> {
                    // Energy wavy/horizontal lines
                    val ySpacing = 5.dp.toPx()
                    val startY = h * 0.25f
                    for (i in 0..3) {
                        val lengthFactor = when (i) {
                            0 -> 0.5f
                            1 -> 0.7f
                            2 -> 0.6f
                            else -> 0.4f
                        }
                        drawLine(
                            color = color,
                            start = Offset(w * 0.15f, startY + i * ySpacing),
                            end = Offset(w * (0.15f + lengthFactor), startY + i * ySpacing),
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Round
                        )
                    }
                }

                SleepIconType.Briefcase -> {
                    // Tools briefcase
                    drawRoundRect(
                        color = color,
                        topLeft = Offset(w * 0.15f, h * 0.32f),
                        size = Size(w * 0.7f, h * 0.52f),
                        cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx()),
                        style = Stroke(width = strokeWidth)
                    )
                    drawRoundRect(
                        color = color,
                        topLeft = Offset(w * 0.32f, h * 0.15f),
                        size = Size(w * 0.36f, h * 0.18f),
                        cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
                        style = Stroke(width = strokeWidth)
                    )
                }

                SleepIconType.LineChart -> {
                    // Progress line chart
                    val path = androidx.compose.ui.graphics.Path().apply {
                        moveTo(w * 0.15f, h * 0.75f)
                        quadraticTo(
                            w * 0.45f, h * 0.75f,
                            w * 0.55f, h * 0.45f
                        )
                        lineTo(w * 0.85f, h * 0.3f)
                    }
                    drawPath(
                        path = path,
                        color = color,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }

                SleepIconType.Lightbulb -> {
                    // Learn lightbulb
                    drawCircle(
                        color = color,
                        radius = w * 0.26f,
                        center = Offset(w * 0.5f, h * 0.38f),
                        style = Stroke(width = strokeWidth)
                    )
                    drawLine(
                        color = color,
                        start = Offset(w * 0.38f, h * 0.7f),
                        end = Offset(w * 0.62f, h * 0.7f),
                        strokeWidth = strokeWidth
                    )
                    drawLine(
                        color = color,
                        start = Offset(w * 0.42f, h * 0.78f),
                        end = Offset(w * 0.58f, h * 0.78f),
                        strokeWidth = strokeWidth
                    )
                    drawLine(
                        color = color,
                        start = Offset(w * 0.3f, h * 0.54f),
                        end = Offset(w * 0.38f, h * 0.7f),
                        strokeWidth = strokeWidth
                    )
                    drawLine(
                        color = color,
                        start = Offset(w * 0.7f, h * 0.54f),
                        end = Offset(w * 0.62f, h * 0.7f),
                        strokeWidth = strokeWidth
                    )
                }
            }
        }
    }
}

enum class SleepIconType {
    Bell,
    Profile,
    Target,
    Lines,
    Briefcase,
    LineChart,
    Lightbulb
}
