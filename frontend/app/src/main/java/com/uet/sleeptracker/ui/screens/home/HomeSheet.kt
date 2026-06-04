package com.uet.sleeptracker.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import androidx.compose.ui.tooling.preview.Preview
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

private val SheetPanelColor = Color(0xFF232429)
private val SheetBorderColor = Color(0xFF55565C)
private val SheetItemsShape = RoundedCornerShape(16.dp)
private val SheetItemMinHeight = 72.dp

@Composable
internal fun SheetToolItem(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String?,
    showBell: Boolean = true,
    titleColor: Color = Color.White,
    onClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = SheetItemMinHeight)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
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
                Icon(
                    painter = painterResource(id = R.drawable.bell),
                    contentDescription = "Reminder",
                    tint = Color.White.copy(alpha = 0.6f),
                    modifier = Modifier.size(18.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.right),
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.4f),
                    modifier = Modifier.size(20.dp)
                )
            }
        } else {
            if (title != "Browse all tools") {
                Icon(
                    painter = painterResource(id = R.drawable.right),
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.4f),
                    modifier = Modifier.size(20.dp)
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
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.double_up),
            contentDescription = if (isExpanded) "Collapse panel" else "Expand panel",
            tint = Color.White,
            modifier = Modifier
                .size(28.dp)
                .clickable(
                    role = Role.Button,
                    onClick = onToggle
                )
                .rotate(if (isExpanded) 180f else 0f)
        )

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
                Icon(
                    painter = painterResource(id = R.drawable.alarm),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
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
internal fun ColumnScope.SheetExpandedContent(
    modifier: Modifier = Modifier,
    onPeaksAndDipsClick: () -> Unit = {},
    onCaffeineCutoffClick: () -> Unit = {},
    onEveningRoutineClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .weight(1f)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(SheetItemsShape)
                    .background(SheetPanelColor)
                    .border(width = 1.dp, color = SheetBorderColor, shape = SheetItemsShape)
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
                        showBell = true,
                        onClick = onPeaksAndDipsClick
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
                        showBell = true,
                        onClick = onCaffeineCutoffClick
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
                        showBell = true,
                        onClick = onEveningRoutineClick
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

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF1C1C1E))
                    ) {
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

@Composable
private fun Divider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(SheetBorderColor)
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF121212, widthDp = 402, heightDp = 600)
@Composable
private fun HomeSheetPreview() {
    SleeptrackerTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF121212))
                .padding(16.dp)
        ) {
            SheetHeader(
                isExpanded = true,
                onToggle = {}
            )
            Spacer(modifier = Modifier.height(16.dp))
            SheetExpandedContent()
        }
    }
}
