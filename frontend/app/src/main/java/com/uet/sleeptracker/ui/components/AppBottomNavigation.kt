package com.uet.sleeptracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.ui.theme.SleepPrimary

@Composable
fun AppBottomNavigation(
    selectedTab: String,
    onNavigateToHome: () -> Unit = {},
    onNavigateToTools: () -> Unit = {},
    onNavigateToEnergy: () -> Unit = {},
    onNavigateToStats: () -> Unit = {}
) {
    Box(
        modifier = Modifier
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
                IconButton(onClick = onNavigateToHome) {
                    BottomNavItem(type = "bed", isSelected = selectedTab == "home")
                }
                IconButton(onClick = onNavigateToEnergy) {
                    BottomNavItem(type = "lightning", isSelected = selectedTab == "energy")
                }
                IconButton(onClick = onNavigateToTools) {
                    BottomNavItem(type = "toolbox", isSelected = selectedTab == "tools")
                }
                IconButton(onClick = onNavigateToStats) {
                    BottomNavItem(type = "chart", isSelected = selectedTab == "stats")
                }
            }
        }
    }
}

@Composable
private fun BottomNavItem(type: String, isSelected: Boolean) {
    val color = if (isSelected) SleepPrimary else Color(0xFF8E8E93)
    IconPlaceholder(color = color, size = 28.dp, type = type)
}

@Composable
fun IconPlaceholder(color: Color, size: Dp, type: String) {
    Box(
        modifier = Modifier
            .size(size),
        contentAlignment = Alignment.Center
    ) {
        when (type) {
            "bell" -> {
                Box(modifier = Modifier.size(size * 0.7f).border(2.dp, color, RoundedCornerShape(topStart = size, topEnd = size)))
            }
            "profile" -> {
                Box(modifier = Modifier.size(size).border(2.dp, color, CircleShape), contentAlignment = Alignment.Center) {
                    Box(modifier = Modifier.size(size * 0.4f).border(2.dp, color, CircleShape))
                }
            }
            "bed" -> {
                Box(modifier = Modifier.size(size * 0.8f, size * 0.5f).border(2.dp, color, RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)))
            }
            "lightning" -> {
                Box(modifier = Modifier.size(size * 0.5f, size * 0.8f).background(color, RoundedCornerShape(2.dp)))
            }
            "toolbox" -> {
                Box(modifier = Modifier.size(size * 0.8f, size * 0.6f).border(2.dp, color, RoundedCornerShape(4.dp)), contentAlignment = Alignment.TopCenter) {
                    Box(modifier = Modifier.size(size * 0.3f, size * 0.2f).offset(y = (-4).dp).border(2.dp, color, RoundedCornerShape(2.dp)))
                }
            }
            "chart" -> {
                Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.size(size * 0.8f)) {
                    Box(modifier = Modifier.weight(1f).height(size * 0.3f).background(color))
                    Spacer(modifier = Modifier.width(2.dp))
                    Box(modifier = Modifier.weight(1f).height(size * 0.6f).background(color))
                    Spacer(modifier = Modifier.width(2.dp))
                    Box(modifier = Modifier.weight(1f).height(size * 0.4f).background(color))
                }
            }
        }
    }
}
