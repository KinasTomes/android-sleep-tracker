package com.uet.sleeptracker.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.uet.sleeptracker.ui.components.ScreenTopBar
import com.uet.sleeptracker.ui.components.SleepBottomNavTab
import com.uet.sleeptracker.ui.components.SleepBottomNavigation
import com.uet.sleeptracker.ui.components.SleepIconType
import com.uet.sleeptracker.ui.components.SleepTopActionIcon
import com.uet.sleeptracker.ui.screens.home.HomeScreen
import com.uet.sleeptracker.ui.screens.home.HomeSlideUpPanel
import androidx.compose.ui.tooling.preview.Preview
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
    onSmartAlarmClick: () -> Unit = {},
    onPrepBedroomClick: () -> Unit = {},
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
                            HomeScreen(modifier = Modifier.fillMaxSize())
                        }

                        SleepBottomNavTab.Tools -> {
                            ToolsScreen(
                                modifier = Modifier.fillMaxSize(),
                                onSmartAlarmClick = onSmartAlarmClick,
                                onPrepBedroomClick = onPrepBedroomClick
                            )
                        }

                        else -> {
                            HomeScreen(modifier = Modifier.fillMaxSize())
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
                onEveningRoutineClick = onEveningRoutineClick
            )

            SleepBottomNavigation(
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    selectedTab = when (tab) {
                        SleepBottomNavTab.MySleep,
                        SleepBottomNavTab.Tools -> tab

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

    ScreenTopBar(
        mainTitle = if (isTools) "Tools" else "My Sleep",
        subTitle = if (isTools) null else "May 17-30",
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
