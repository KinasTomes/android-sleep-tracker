package com.uet.sleeptracker.ui.screens.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.ui.components.ScreenTopBar
import com.uet.sleeptracker.ui.components.SleepBottomNavTab
import com.uet.sleeptracker.ui.components.SleepBottomNavigation
import com.uet.sleeptracker.ui.components.SleepIconType
import com.uet.sleeptracker.ui.components.SleepTopActionIcon
import com.uet.sleeptracker.ui.theme.SleepBackground
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    dateRangeText: String = "May 17-30",
    sleepDebtHours: Float = 1.2f,
    debtSinceYesterdayText: String = "-8hrs",
    energyPotentialText: String = "84%",
    totalSleepTimeText: String = "8h 30m",
    sleepTimeRangeText: String = "23:30 - 08:00",
    napsTodayText: String = "",
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
                    bottom = 90.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    ScreenTopBar(
                        mainTitle = "My Sleep",
                        subTitle = dateRangeText,
                        actions = {
                            SleepTopActionIcon(
                                type = SleepIconType.Profile,
                                onClick = onProfileClick,
                                tint = Color(0xFF7C5CFF),
                                size = 30.dp
                            )
                        }
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
                            fontWeight = FontWeight.SemiBold
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

                    SheetHeader(
                        isExpanded = isSheetExpanded,
                        onToggle = { isSheetExpanded = !isSheetExpanded }
                    )

                    if (isSheetExpanded) {
                        SheetExpandedContent()
                    }
                }
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
