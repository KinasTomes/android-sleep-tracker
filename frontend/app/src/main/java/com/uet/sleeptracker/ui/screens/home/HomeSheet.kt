package com.uet.sleeptracker.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun SheetToolItem(
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

@Composable
internal fun SheetHeader(
    isExpanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Double Chevron indicator
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy((-10).dp)
        ) {
            val chevronChar = if (isExpanded) "﹀" else "︿"
            Text(text = chevronChar, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = chevronChar, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
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
                Text(text = "⏰", color = Color.White, fontSize = 15.sp)
                Text(text = "08:00", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            // Plus Button
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2C2C2E)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "+", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Normal)
            }
        }
    }
}

@Composable
internal fun ColumnScope.SheetExpandedContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
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
                SheetToolItem(
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Brush.linearGradient(listOf(Color(0xFFF97316), Color(0xFF8B5CF6))))
                        ) {
                            Text("〰️", color = Color.White, modifier = Modifier.align(Alignment.Center), fontSize = 12.sp)
                        }
                    },
                    title = "Peaks & dips",
                    subtitle = "09:00",
                    showBell = true
                )
                Divider()

                SheetToolItem(
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF78350F)),
                            contentAlignment = Alignment.Center
                        ) { Text("☕", fontSize = 14.sp) }
                    },
                    title = "Avoid caffeine",
                    subtitle = "12:45",
                    showBell = true
                )
                Divider()

                SheetToolItem(
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF6B21A8)),
                            contentAlignment = Alignment.Center
                        ) { Text("📋", fontSize = 14.sp) }
                    },
                    title = "Evening routine",
                    subtitle = "20:35–22:45",
                    showBell = true
                )
                Divider()

                SheetToolItem(
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF1E1B4B)),
                            contentAlignment = Alignment.Center
                        ) { Text("🌙", fontSize = 14.sp) }
                    },
                    title = "Melatonin window",
                    subtitle = "22:45–00:15",
                    showBell = true
                )
                Divider()

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

@Composable
private fun Divider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color.White.copy(alpha = 0.06f))
    )
}
