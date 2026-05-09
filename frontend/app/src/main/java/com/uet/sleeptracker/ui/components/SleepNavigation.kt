package com.uet.sleeptracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.uet.sleeptracker.ui.theme.SleepPrimary

enum class SleepBottomNavTab {
    MySleep,
    Energy,
    Tools,
    Stats
}

@Composable
fun SleepBottomNavigation(
    selectedTab: SleepBottomNavTab,
    onTabSelected: (SleepBottomNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SleepBottomNavTab.entries.forEach { tab ->
                    SleepBottomNavigationItem(
                        tab = tab,
                        isSelected = tab == selectedTab,
                        onClick = { onTabSelected(tab) }
                    )
                }
            }
        }
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
        when (type) {
            SleepIconType.Bell -> {
                Box(
                    modifier = Modifier
                        .size(size * 0.62f, size * 0.58f)
                        .border(2.dp, color, RoundedCornerShape(topStart = size, topEnd = size))
                )
                Box(
                    modifier = Modifier
                        .size(size * 0.1f)
                        .align(Alignment.BottomCenter)
                        .offset(y = (-2).dp)
                        .background(color, CircleShape)
                )
            }

            SleepIconType.Profile -> {
                Box(
                    modifier = Modifier
                        .size(size)
                        .border(2.dp, color, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(size * 0.34f)
                            .offset(y = (-size * 0.14f))
                            .border(2.dp, color, CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .size(size * 0.5f, size * 0.24f)
                            .align(Alignment.BottomCenter)
                            .offset(y = (-size * 0.18f))
                            .border(
                                width = 2.dp,
                                color = color,
                                shape = RoundedCornerShape(topStart = size, topEnd = size)
                            )
                    )
                }
            }

            SleepIconType.Bed -> {
                Box(
                    modifier = Modifier
                        .size(size * 0.82f, size * 0.48f)
                        .border(2.dp, color, RoundedCornerShape(4.dp))
                        .align(Alignment.BottomCenter)
                )
                Box(
                    modifier = Modifier
                        .size(size * 0.18f, size * 0.5f)
                        .align(Alignment.CenterStart)
                        .offset(y = size * 0.06f)
                        .background(color)
                )
                Box(
                    modifier = Modifier
                        .size(size * 0.26f, size * 0.18f)
                        .align(Alignment.Center)
                        .offset(x = (-size * 0.08f))
                        .border(2.dp, color, RoundedCornerShape(3.dp))
                )
            }

            SleepIconType.Lightning -> {
                Box(
                    modifier = Modifier
                        .size(size * 0.22f, size * 0.62f)
                        .offset(x = (-size * 0.06f), y = (-size * 0.06f))
                        .background(color, RoundedCornerShape(2.dp))
                )
                Box(
                    modifier = Modifier
                        .size(size * 0.22f, size * 0.5f)
                        .offset(x = (size * 0.06f), y = size * 0.1f)
                        .background(color, RoundedCornerShape(2.dp))
                )
            }

            SleepIconType.Toolbox -> {
                Box(
                    modifier = Modifier
                        .size(size * 0.8f, size * 0.52f)
                        .border(2.dp, color, RoundedCornerShape(4.dp))
                )
                Box(
                    modifier = Modifier
                        .size(size * 0.28f, size * 0.18f)
                        .align(Alignment.TopCenter)
                        .offset(y = (-size * 0.1f))
                        .border(2.dp, color, RoundedCornerShape(3.dp))
                )
            }

            SleepIconType.Chart -> {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.size(size * 0.82f)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(size * 0.3f)
                            .background(color)
                    )
                    Box(modifier = Modifier.size(2.dp))
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(size * 0.6f)
                            .background(color)
                    )
                    Box(modifier = Modifier.size(2.dp))
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(size * 0.42f)
                            .background(color)
                    )
                }
            }
        }
    }
}

enum class SleepIconType {
    Bell,
    Profile,
    Bed,
    Lightning,
    Toolbox,
    Chart
}

@Composable
private fun SleepBottomNavigationItem(
    tab: SleepBottomNavTab,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val color = if (isSelected) SleepPrimary else Color(0xFF8E8E93)
    val iconType = when (tab) {
        SleepBottomNavTab.MySleep -> SleepIconType.Bed
        SleepBottomNavTab.Energy -> SleepIconType.Lightning
        SleepBottomNavTab.Tools -> SleepIconType.Toolbox
        SleepBottomNavTab.Stats -> SleepIconType.Chart
    }

    IconButton(onClick = onClick) {
        SleepIconPlaceholder(
            color = color,
            size = 28.dp,
            type = iconType
        )
    }
}
