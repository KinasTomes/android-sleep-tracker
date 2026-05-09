package com.uet.sleeptracker.ui.screens.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.components.SleepBottomNavTab
import com.uet.sleeptracker.ui.components.SleepBottomNavigation
import com.uet.sleeptracker.ui.components.SleepIconPlaceholder
import com.uet.sleeptracker.ui.components.SleepIconType
import com.uet.sleeptracker.ui.components.SleepTopActionIcon
import com.uet.sleeptracker.ui.theme.SleepBackground
import com.uet.sleeptracker.ui.theme.SleepOnSurfaceVariant
import com.uet.sleeptracker.ui.theme.SleepSurface
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

@Composable
fun ToolsScreen(
    modifier: Modifier = Modifier,
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onNavigateToHome: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SleepBackground,
        bottomBar = {
            AppBottomNavigation(
                selectedTab = "tools",
                onNavigateToHome = onNavigateToHome
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                ToolsTopBar(
                    onNotificationClick = onNotificationClick,
                    onProfileClick = onProfileClick
                )
            }

            item {
                SleepDetectionCard()
            }

            item {
                SectionHeader(title = stringResource(R.string.tools_sleep_aids_title))
                Spacer(modifier = Modifier.height(16.dp))
                SleepAidsPlayerCard()
            }

            item {
                Text(
                    text = stringResource(R.string.tools_guides_title),
                    color = SleepOnSurfaceVariant,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GuideCard(
                        modifier = Modifier.weight(1f),
                        title = stringResource(R.string.tools_write_brain_dump_title),
                        description = stringResource(R.string.tools_write_brain_dump_desc),
                        purpleColor = Color(0xFF5E5CE6)
                    )
                    GuideCard(
                        modifier = Modifier.weight(1f),
                        title = stringResource(R.string.tools_prep_bedroom_title),
                        description = stringResource(R.string.tools_prep_bedroom_desc),
                        purpleColor = Color(0xFF5E5CE6).copy(alpha = 0.7f)
                    )
                }
            }

            item {
                SectionHeader(title = stringResource(R.string.tools_habit_reminders_title))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.tools_habit_reminders_desc),
                    color = SleepOnSurfaceVariant,
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
private fun ToolsTopBar(
    onNotificationClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.tools_title),
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            SleepTopActionIcon(
                type = SleepIconType.Bell,
                onClick = onNotificationClick,
                tint = Color(0xFF721BFD),
                size = 28.dp
            )
            SleepTopActionIcon(
                type = SleepIconType.Profile,
                onClick = onProfileClick,
                tint = Color(0xFF721BFD),
                size = 32.dp
            )
        }
    }
}

@Composable
private fun SleepDetectionCard() {
    Column(
        modifier = Modifier
            .width(220.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(SleepSurface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(Color(0xFF2C1C8F)),
            contentAlignment = Alignment.Center
        ) {
            // Graphic placeholder
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Zzz", color = Color.White.copy(alpha = 0.5f), fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    repeat(4) {
                        Box(
                            modifier = Modifier
                                .width(30.dp - (it * 4).dp)
                                .height(2.dp)
                                .background(Color.White.copy(alpha = 0.5f))
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.tools_sleep_detection_title),
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.tools_sleep_detection_desc),
                color = SleepOnSurfaceVariant,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun SleepAidsPlayerCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = SleepSurface,
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "Billie Bossa Nova - Billie Eilish",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Happier Than Ever",
                color = SleepOnSurfaceVariant,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(Color(0xFF3A3A3C), RoundedCornerShape(2.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .fillMaxHeight()
                            .background(Color.White, RoundedCornerShape(2.dp))
                    )
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .align(Alignment.CenterStart)
                            .offset(x = 135.dp) // Approximately 40%
                            .background(Color.White, CircleShape)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "0:38", color = SleepOnSurfaceVariant, fontSize = 11.sp)
                    Text(text = "3:16", color = SleepOnSurfaceVariant, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
private fun GuideCard(
    title: String,
    description: String,
    purpleColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(SleepSurface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(purpleColor)
        )
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                color = SleepOnSurfaceVariant,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        color = Color.White,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun ToolsBottomNavigation() {
    SleepBottomNavigation(
        selectedTab = SleepBottomNavTab.Tools,
        onTabSelected = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun ToolsScreenPreview() {
    SleeptrackerTheme {
        ToolsScreen()
    }
}
