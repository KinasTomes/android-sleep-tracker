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
fun EarlierWorkoutsScreen(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    onGetStartedClick: () -> Unit = {},
) {
    HabitIntroductionScreen(
        content = HabitIntroductionContent(
            topBarTitle = "Earlier Workouts",
            heroTitle = "Earlier Workouts",
            heroDescription = "Working out can help you sleep\nbetter, however timing matters.",
            heroImageRes = R.drawable.earlier_workouts_banner,
            sectionTitle = "Tips",
            bullets = listOf(
                "Try to end workouts 1 hour before bedtime.",
                "Working out during the day, even as little as 10 minutes, can improve your sleep.",
            ),
            explanationTitle = "The Science Behind It",
            explanationParagraphs = listOf(
                "The research behind the effect of late-night exercise on sleep isn't conclusive. Experimental studies have failed to prove the link. However, this result has varied by individual.",
            ),
        ),
        onCloseClick = onCloseClick,
        onGetStartedClick = onGetStartedClick,
        modifier = modifier,
    )
}

@Composable
fun EarlierWorkoutsSettingsScreen(
    modifier: Modifier = Modifier,
    reminderTime: String = "1h",
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
            topBarTitle = "Earlier Workouts",
            heroTitle = "Earlier Workouts",
            heroIconRes = R.drawable.workouts,
            heroGradientEnd = Color(0xFF06602A),
            toggleTitle = "Remind me",
            toggleSubtitle = "to workout earlier",
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
private fun EarlierWorkoutsScreenPreview() {
    SleeptrackerTheme {
        EarlierWorkoutsScreen()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun EarlierWorkoutsSettingsScreenPreview() {
    SleeptrackerTheme {
        EarlierWorkoutsSettingsScreen()
    }
}
