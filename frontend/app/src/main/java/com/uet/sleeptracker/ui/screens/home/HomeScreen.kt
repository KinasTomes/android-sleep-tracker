package com.uet.sleeptracker.ui.screens.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.ui.components.SleepBottomNavTab
import com.uet.sleeptracker.ui.components.SleepBottomNavigation
import com.uet.sleeptracker.ui.components.SleepIconPlaceholder
import com.uet.sleeptracker.ui.components.SleepIconType
import com.uet.sleeptracker.ui.components.SleepTopActionIcon
import com.uet.sleeptracker.ui.theme.SleepBackground
import com.uet.sleeptracker.ui.theme.SleepOnSurfaceVariant
import com.uet.sleeptracker.ui.theme.SleepPrimary
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    dateRangeText: String = "May 17-30",
    sleepDebtHours: Float = 3.3f,
    debtSinceYesterdayText: String = "-hrs",
    energyPotentialText: String = "84%",
    totalSleepTimeText: String = "8h 30m",
    sleepTimeRangeText: String = "23:30 - 08:00",
    napsTodayText: String = "",
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onBottomNavSelected: (SleepBottomNavTab) -> Unit = {}
) {
    var isSheetExpanded by remember { mutableStateOf(false) }
    val sheetHeight by animateDpAsState(targetValue = if (isSheetExpanded) 490.dp else 76.dp, label = "sheetHeightAnim")

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SleepBackground,
        bottomBar = {
            SleepBottomNavigation(
                selectedTab = SleepBottomNavTab.MySleep,
                onTabSelected = onBottomNavSelected
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main content
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(
                    top = 18.dp,
                    bottom = 90.dp // padding to avoid being completely covered by the sheet
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    HomeTopBar(
                        dateRangeText = dateRangeText,
                        onNotificationClick = onNotificationClick,
                        onProfileClick = onProfileClick
                    )
                }

                item {
                    SleepDebtHero(sleepDebtHours = sleepDebtHours)
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DebtSummary(
                            value = debtSinceYesterdayText,
                            modifier = Modifier.weight(1f)
                        )
                        EnergyPotentialCard(
                            value = energyPotentialText,
                            modifier = Modifier.weight(1.1f)
                        )
                    }
                }

                item {
                    LastNightCard(
                        totalSleepTime = totalSleepTimeText,
                        sleepTimeRange = sleepTimeRangeText
                    )
                }

                item {
                    NapsTodayCard(napsTodayText = napsTodayText)
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Sleep tracking unreliable?",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Help improve RISE",
                            color = Color(0xFF7C5CFF),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable { /* improve action */ }
                        )
                    }
                }
            }

            // Draggable slide-up sheet overlay
            Surface(
                color = Color(0xFF151517),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(sheetHeight)
                    .align(Alignment.BottomCenter)
                    .pointerInput(Unit) {
                        detectVerticalDragGestures { change, dragAmount ->
                            change.consume()
                            if (dragAmount < -12) {
                                isSheetExpanded = true
                            } else if (dragAmount > 12) {
                                isSheetExpanded = false
                            }
                        }
                    }
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Drag Handle
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .width(36.dp)
                                .height(4.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f))
                        )
                    }

                    // Header Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isSheetExpanded = !isSheetExpanded }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Double Chevron indicator
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy((-10).dp)
                        ) {
                            val chevronChar = if (isSheetExpanded) "﹀" else "︿"
                            Text(
                                text = chevronChar,
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = chevronChar,
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Right actions
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Clock Pill button
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color(0xFF7C5CFF))
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "⏰",
                                    color = Color.White,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "08:00",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Plus Button
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF2C2C2E)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "+",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }
                    }

                    // Scrollable expanded items
                    if (isSheetExpanded) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                                .padding(bottom = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Surface(
                                color = Color(0xFF1C1C1E),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    // Peaks & dips
                                    SheetToolItem(
                                        icon = {
                                            Box(
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(
                                                        Brush.linearGradient(
                                                            colors = listOf(Color(0xFFF97316), Color(0xFF8B5CF6))
                                                        )
                                                    )
                                            ) {
                                                Text("〰️", color = Color.White, modifier = Modifier.align(Alignment.Center), fontSize = 12.sp)
                                            }
                                        },
                                        title = "Peaks & dips",
                                        subtitle = "09:00",
                                        showBell = true
                                    )
                                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.06f)))

                                    // Avoid caffeine
                                    SheetToolItem(
                                        icon = {
                                            Box(
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(Color(0xFF78350F)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text("☕", fontSize = 14.sp)
                                            }
                                        },
                                        title = "Avoid caffeine",
                                        subtitle = "12:45",
                                        showBell = true
                                    )
                                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.06f)))

                                    // Evening routine
                                    SheetToolItem(
                                        icon = {
                                            Box(
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(Color(0xFF6B21A8)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text("📋", fontSize = 14.sp)
                                            }
                                        },
                                        title = "Evening routine",
                                        subtitle = "20:35–22:45",
                                        showBell = true
                                    )
                                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.06f)))

                                    // Melatonin window
                                    SheetToolItem(
                                        icon = {
                                            Box(
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(Color(0xFF1E1B4B)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text("🌙", fontSize = 14.sp)
                                            }
                                        },
                                        title = "Melatonin window",
                                        subtitle = "22:45–00:15",
                                        showBell = true
                                    )
                                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.06f)))

                                    // Browse all tools
                                    SheetToolItem(
                                        icon = {
                                            Text(
                                                text = "+",
                                                color = Color(0xFFC084FC),
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.Light,
                                                modifier = Modifier.padding(horizontal = 4.dp)
                                            )
                                        },
                                        title = "Browse all tools",
                                        subtitle = null,
                                        showBell = false,
                                        titleColor = Color(0xFFC084FC)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeTopBar(
    dateRangeText: String,
    onNotificationClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp, start = 8.dp, end = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "My Sleep",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = dateRangeText,
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 16.sp
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Gift icon outline with badge 2
            GiftIconWithBadge(
                count = 2,
                onClick = onNotificationClick
            )
            
            // Profile icon from components
            SleepTopActionIcon(
                type = SleepIconType.Profile,
                onClick = onProfileClick,
                tint = Color(0xFF7C5CFF),
                size = 32.dp
            )
        }
    }
}

@Composable
fun GiftIconOutline(
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(24.dp)) {
        val strokeWidth = 2.dp.toPx()
        // Draw box base
        drawRect(
            color = color,
            topLeft = Offset(4.dp.toPx(), 9.dp.toPx()),
            size = Size(16.dp.toPx(), 11.dp.toPx()),
            style = Stroke(width = strokeWidth)
        )
        // Draw box lid
        drawRect(
            color = color,
            topLeft = Offset(2.dp.toPx(), 6.dp.toPx()),
            size = Size(20.dp.toPx(), 3.dp.toPx()),
            style = Stroke(width = strokeWidth)
        )
        // Draw vertical ribbon
        drawLine(
            color = color,
            start = Offset(12.dp.toPx(), 6.dp.toPx()),
            end = Offset(12.dp.toPx(), 20.dp.toPx()),
            strokeWidth = strokeWidth
        )
        // Draw bow left loop
        drawArc(
            color = color,
            startAngle = 135f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(7.dp.toPx(), 2.dp.toPx()),
            size = Size(5.dp.toPx(), 5.dp.toPx()),
            style = Stroke(width = strokeWidth)
        )
        // Draw bow right loop
        drawArc(
            color = color,
            startAngle = 225f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(12.dp.toPx(), 2.dp.toPx()),
            size = Size(5.dp.toPx(), 5.dp.toPx()),
            style = Stroke(width = strokeWidth)
        )
    }
}

@Composable
fun GiftIconWithBadge(
    count: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        GiftIconOutline(color = Color(0xFF7C5CFF), modifier = Modifier.size(28.dp))
        if (count > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 6.dp, y = (-4).dp)
                    .size(16.dp)
                    .background(Color(0xFF7C5CFF), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = count.toString(),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

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
private fun SleepDebtHero(sleepDebtHours: Float) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(340.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF232429))
    ) {
        // Viewport: x = 0..8, y = -4..4
        val vpW = 8f
        val vpH = 8f
        val scaleX = size.width / vpW
        val scaleY = size.height / vpH

        // Chuyển tọa độ toán học -> canvas pixels
        // x_math=0 -> canvas x=0 (cạnh trái)
        // y_math=0 -> canvas y = size.height/2 (giữa)
        fun toCanvas(x: Float, y: Float) = Offset(
            x = x * scaleX,
            y = (vpH / 2f - y) * scaleY
        )

        data class C(val r: Float, val dx: Float)
        val circles = listOf(
            C(r = 64f / (8f * 0.7f) + 0.7f / 2f,  dx = 2.1f),
            C(r = 64f / (8f * 1.75f) + 1.75f / 2f, dx = 5.5f),
            C(r = 64f / (8f * 2.5f) + 2.5f / 2f,   dx = 8f)
        )

        for (c in circles) {
            val centerX = c.dx - c.r
            val centerCanvas = toCanvas(centerX, 0f)
            val radiusPx = c.r * scaleX

            drawArc(
                color = Color.Black,
                startAngle = -90f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(centerCanvas.x - radiusPx, centerCanvas.y - radiusPx),
                size = Size(radiusPx * 2, radiusPx * 2),
                style = Stroke(width = 2.dp.toPx())
            )
        }

        // Dynamic curve based on sleepDebtHours
        val config = calculateCurveConfig(sleepDebtHours)
        val dynCenterX = config.distanceX - config.radius
        val dynCenter = toCanvas(dynCenterX, 0f)
        val dynRadiusPx = config.radius * scaleX

        val purple = Color(0xFF7C5CFF)
        val dynBrush = if (sleepDebtHours <= 4.8f) {
            // top -> mid -> bottom: purple -> teal-blue -> turquoise -> teal-blue -> purple
            Brush.verticalGradient(
                colors = listOf(
                    purple,
                    Color(0xFF2EABD0),
                    Color(0xFF08F0B6),
                    Color(0xFF2EABD0),
                    purple
                ),
                startY = dynCenter.y - dynRadiusPx,
                endY = dynCenter.y + dynRadiusPx
            )
        } else {
            // top -> mid -> bottom: magenta -> red/pink -> orange -> red/pink -> magenta
            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFB141BF),
                    Color(0xFFCB676F),
                    Color(0xFFFD9B03),
                    Color(0xFFCB676F),
                    Color(0xFFB141BF)
                ),
                startY = dynCenter.y - dynRadiusPx,
                endY = dynCenter.y + dynRadiusPx
            )
        }

        drawArc(
            brush = dynBrush,
            startAngle = -90f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(dynCenter.x - dynRadiusPx, dynCenter.y - dynRadiusPx),
            size = Size(dynRadiusPx * 2, dynRadiusPx * 2),
            style = Stroke(width = 4.dp.toPx())
        )
    }
}

@Composable
private fun DebtSummary(
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(start = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF2C2C2E))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = "$value debt",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Since yesterday",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 13.sp
            )
        }
    }
}

@Composable
private fun EnergyPotentialCard(
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(0xFF1C1C1E),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "Energy Potential",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = value,
                    color = Color(0xFF2DD4BF),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Text(
                text = ">",
                color = Color.White.copy(alpha = 0.4f),
                fontSize = 18.sp
            )
        }
    }
}

@Composable
private fun LastNightCard(
    totalSleepTime: String,
    sleepTimeRange: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "LAST NIGHT",
            color = Color.White.copy(alpha = 0.4f),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
        )
        Surface(
            color = Color(0xFF1C1C1E),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Row 1: Total Sleep Time & range
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = totalSleepTime,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Total Sleep Time",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 14.sp
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = sleepTimeRange,
                            color = Color.White,
                            fontSize = 14.sp
                        )
                        Text(
                            text = ">",
                            color = Color.White.copy(alpha = 0.4f),
                            fontSize = 18.sp
                        )
                    }
                }

                // Divider line
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.White.copy(alpha = 0.08f))
                )

                // Row 2: Self-Reported Sleep Pattern
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF3F1D5A)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "👤",
                            color = Color(0xFFC084FC),
                            fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Self-Reported Sleep Pattern",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "i",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NapsTodayCard(
    napsTodayText: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(0xFF1C1C1E),
        shape = RoundedCornerShape(14.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Naps Today",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = if (napsTodayText.isEmpty()) "Add Time" else napsTodayText,
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 14.sp
                )
                Text(
                    text = ">",
                    color = Color.White.copy(alpha = 0.4f),
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
private fun SheetToolItem(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String?,
    showBell: Boolean = true,
    titleColor: Color = Color.White,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            icon()
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    color = titleColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                if (subtitle != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 14.sp
                    )
                }
            }
        }

        if (showBell) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "🔔",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 16.sp
                )
                Text(
                    text = ">",
                    color = Color.White.copy(alpha = 0.4f),
                    fontSize = 18.sp
                )
            }
        } else {
            if (title != "Browse all tools") {
                Text(
                    text = ">",
                    color = Color.White.copy(alpha = 0.4f),
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 402, heightDp = 874)
@Composable
private fun HomeScreenPreview() {
    SleeptrackerTheme {
        HomeScreen()
    }
}
