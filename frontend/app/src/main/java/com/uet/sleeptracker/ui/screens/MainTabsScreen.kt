package com.uet.sleeptracker.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.components.ScreenTopBar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.uet.sleeptracker.ui.components.SleepBottomNavTab
import com.uet.sleeptracker.ui.components.SleepBottomNavigation
import com.uet.sleeptracker.ui.components.SleepIconType
import com.uet.sleeptracker.ui.components.SleepTopActionIcon
import com.uet.sleeptracker.ui.screens.energy.EnergyScheduleScreen
import com.uet.sleeptracker.ui.screens.home.HomeScreen
import com.uet.sleeptracker.ui.screens.home.HomeSlideUpPanel
import androidx.compose.ui.tooling.preview.Preview
import com.uet.sleeptracker.ui.screens.progress.ProgressScreen
import com.uet.sleeptracker.ui.screens.tools.ToolsScreen
import com.uet.sleeptracker.ui.theme.SleepBackground
import com.uet.sleeptracker.ui.theme.SleepPrimary
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

@Composable
fun MainTabsScreen(
    modifier: Modifier = Modifier,
    onProfileClick: () -> Unit = {},
    onPeaksAndDipsClick: () -> Unit = {},
    onCaffeineCutoffClick: () -> Unit = {},
    onEveningRoutineClick: () -> Unit = {},
    onMorningRoutineClick: () -> Unit = {},
    onBlueLightControlClick: () -> Unit = {},
    onSmartAlarmClick: () -> Unit = {},
    onSleepQualityClick: () -> Unit = {},
    onPrepBedroomClick: () -> Unit = {},
    onBrainDumpClick: () -> Unit = {},
    onMelatoninWindowClick: () -> Unit = {},
    onMelatoninSupplementsClick: () -> Unit = {},
    onBlueLightGlassesClick: () -> Unit = {},
    onAvoidLateMealsClick: () -> Unit = {},
    onEarlierWorkoutsClick: () -> Unit = {},
    onAvoidLateAlcoholClick: () -> Unit = {},
    onSleepRecordClick: (title: String, startTime: String, endTime: String, duration: String) -> Unit = { _, _, _, _ -> },
) {
    var selectedTab by rememberSaveable { mutableStateOf(SleepBottomNavTab.MySleep) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SleepBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SleepBackground)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = innerPadding.calculateTopPadding(),
                        bottom = 76.dp
                    )
            ) {
                MainTabsTopBar(
                    selectedTab = selectedTab,
                    onProfileClick = onProfileClick,
                )

                AnimatedContent(
                    targetState = selectedTab,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "mainTabsCoreContent"
                ) { tab ->
                    when (tab) {
                        SleepBottomNavTab.MySleep -> {
                            HomeScreen(
                                modifier = Modifier.fillMaxSize(),
                                onNavigateToEnergy = { selectedTab = SleepBottomNavTab.Energy }
                            )
                        }

                        SleepBottomNavTab.Energy -> {
                            EnergyScheduleScreen(
                                modifier = Modifier.fillMaxSize(),
                                profileIcon = ColorPainter(Color.Transparent),
                                lightningIcon = painterResource(id = R.drawable.lightning),
                                sleepIcon = painterResource(id = R.drawable.zzz),
                                editIcon = painterResource(id = R.drawable.scissor),
                                windDownImage = painterResource(id = R.drawable.evening),
                                caffeineImage = painterResource(id = R.drawable.ceffeine),
                                showTopBar = false
                            )
                        }

                        SleepBottomNavTab.Tools -> {
                            ToolsScreen(
                                modifier = Modifier.fillMaxSize(),
                                onSmartAlarmClick = onSmartAlarmClick,
                                onSleepQualityClick = onSleepQualityClick,
                                onPrepBedroomClick = onPrepBedroomClick,
                                onBrainDumpClick = onBrainDumpClick,
                                onMelatoninWindowClick = onMelatoninWindowClick,
                                onMelatoninSupplementsClick = onMelatoninSupplementsClick,
                                onBlueLightGlassesClick = onBlueLightGlassesClick,
                                onAvoidLateMealsClick = onAvoidLateMealsClick,
                                onEarlierWorkoutsClick = onEarlierWorkoutsClick,
                                onAvoidLateAlcoholClick = onAvoidLateAlcoholClick,
                                onCaffeineCutoffClick = onCaffeineCutoffClick,
                                onEveningRoutineClick = onEveningRoutineClick,
                                onMorningRoutineClick = onMorningRoutineClick,
                                onBlueLightControlClick = onBlueLightControlClick,
                                onPeaksAndDipsClick = onPeaksAndDipsClick,
                            )
                        }

                        SleepBottomNavTab.Stats -> {
                            ProgressScreen(
                                modifier = Modifier.fillMaxSize(),
                                onSleepRecordClick = onSleepRecordClick,
                            )
                        }

                        else -> {
                            HomeScreen(
                                modifier = Modifier.fillMaxSize(),
                                onNavigateToEnergy = { selectedTab = SleepBottomNavTab.Energy }
                            )
                        }
                    }
                }
            }

            HomeSlideUpPanel(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding()),
                onPeaksAndDipsClick = onPeaksAndDipsClick,
                onCaffeineCutoffClick = onCaffeineCutoffClick,
                onEveningRoutineClick = onEveningRoutineClick,
                onMelatoninWindowClick = onMelatoninWindowClick,
                onBrowseAllToolsClick = { selectedTab = SleepBottomNavTab.Tools }
            )

            SleepBottomNavigation(
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    selectedTab = when (tab) {
                        SleepBottomNavTab.MySleep,
                        SleepBottomNavTab.Energy,
                        SleepBottomNavTab.Tools,
                        SleepBottomNavTab.Stats -> tab

                        else -> selectedTab
                    }
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun MainTabsTopBar(
    selectedTab: SleepBottomNavTab,
    onProfileClick: () -> Unit,
) {
    val isTools = selectedTab == SleepBottomNavTab.Tools
    val isEnergy = selectedTab == SleepBottomNavTab.Energy
    val isProgress = selectedTab == SleepBottomNavTab.Stats

    ScreenTopBar(
        mainTitle = when {
            isTools -> "Tools"
            isEnergy -> "My energy schedule"
            isProgress -> "My Progress"
            else -> "My Sleep"
        },
        subTitle = when {
            isProgress -> getTopBarDateRange(13)
            isTools || isEnergy -> null
            else -> getTopBarDateRange(13)
        },
        subTitleContent = if (isEnergy) {
            {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Great",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 20.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Normal
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.lightning),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "76%",
                        color = Color(0xFF00EBAC),
                        fontSize = 20.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        } else {
            null
        },
        actions = {
            SleepTopActionIcon(
                type = SleepIconType.Profile,
                onClick = onProfileClick,
                tint = SleepPrimary,
                size = 32.dp
            )
        },
        modifier = Modifier.padding(top = 10.dp),
        horizontalPadding = 19.dp
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun MainTabsScreenPreview() {
    SleeptrackerTheme {
        MainTabsScreen()
    }
}

private fun getTopBarDateRange(daysBack: Int): String {
    val today = Calendar.getInstance()
    val pastDate = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR, -daysBack)
    }

    val monthFormat = SimpleDateFormat("MMM", Locale.US)
    val dayFormat = SimpleDateFormat("d", Locale.US)

    val startMonth = monthFormat.format(pastDate.time)
    val startDay = dayFormat.format(pastDate.time)
    val endMonth = monthFormat.format(today.time)
    val endDay = dayFormat.format(today.time)

    return if (startMonth == endMonth) {
        "$startMonth $startDay-$endDay"
    } else {
        "$startMonth $startDay - $endMonth $endDay"
    }
}
