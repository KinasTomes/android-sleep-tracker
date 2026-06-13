package com.uet.sleeptracker.ui.screens.tools

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.components.HabitIntroductionContent
import com.uet.sleeptracker.ui.components.HabitIntroductionScreen
import com.uet.sleeptracker.ui.components.HabitReminderSettingsScreen
import com.uet.sleeptracker.ui.components.HabitSettingsContent
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

@Composable
fun BlueLightGlassesScreen(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    onGetStartedClick: () -> Unit = {},
) {
    HabitIntroductionScreen(
        content = HabitIntroductionContent(
            topBarTitle = "Wear Orange Glasses",
            heroTitle = "Block All Blue Light",
            heroDescription = "Blue light is everywhere, not just screens. Wearing orange glasses before going to bed helps block blue light, which makes you fall asleep faster.",
            heroImageRes = R.drawable.blue_light_glasses_banner,
            sectionTitle = "Benefits",
            bullets = listOf(
                "Fall asleep faster",
                "Feel more tired at bed time",
                "Feel more refreshed waking up",
            ),
            explanationTitle = "How does this work?",
            explanationParagraphs = listOf(
                "Sundown used to give our body cues to start producing melatonin. Since the introduction of artificial light and blue light emitting screens, people are increasingly having trouble falling asleep.",
            ),
            explanationImageRes = R.drawable.blue_light_glasses_introducing,
        ),
        onCloseClick = onCloseClick,
        onGetStartedClick = onGetStartedClick,
        modifier = modifier,
    )
}

@Composable
fun BlueLightGlassesSettingsScreen(
    modifier: Modifier = Modifier,
    reminderTime: String = "1h 30m",
    initialRemindersEnabled: Boolean = true,
    showActionButtons: Boolean = true,
    onRemindersEnabledChange: (Boolean) -> Unit = {},
    onReminderTimeClick: () -> Unit = {},
    onRemindMeClick: () -> Unit = {},
    onNotNowClick: () -> Unit = {},
    onCloseClick: () -> Unit = {},
) {
    HabitReminderSettingsScreen(
        content = HabitSettingsContent(
            topBarTitle = "Wear Orange Glasses",
            heroTitle = "Wear Orange Glasses",
            heroIconRes = R.drawable.wear_orange_glasses_icon,
            heroGradientEnd = Color(0xFF502D0B),
            toggleTitle = "Remind me",
            toggleSubtitle = "to wear orange glasses",
            reminderValue = reminderTime,
            initialEnabled = initialRemindersEnabled,
            showActions = showActionButtons,
        ),
        onCloseClick = onCloseClick,
        onCheckedChange = onRemindersEnabledChange,
        onReminderTimeClick = onReminderTimeClick,
        onRemindMeClick = onRemindMeClick,
        onNotNowClick = onNotNowClick,
        modifier = modifier,
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun BlueLightGlassesScreenPreview() {
    SleeptrackerTheme {
        BlueLightGlassesScreen()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun BlueLightGlassesSettingsScreenPreview() {
    SleeptrackerTheme {
        BlueLightGlassesSettingsScreen()
    }
}
