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
fun AvoidLateAlcoholScreen(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    onGetStartedClick: () -> Unit = {},
) {
    HabitIntroductionScreen(
        content = HabitIntroductionContent(
            topBarTitle = "Avoid Late Alcohol",
            heroTitle = "Avoid Late Alcohol",
            heroDescription = "Alcohol can help you fall asleep, but greatly compromises sleep quality.",
            heroImageRes = R.drawable.avoid_late_alcohol_banner,
            sectionTitle = "Tips",
            bullets = listOf(
                "Stop consuming alcohol 3 - 4 hours before bedtime",
                "Stay hydrated—drink 2 glasses of water for every drink you consume",
                "Stick to 1 - 2 drinks per night",
                "If you are drinking, have a meal at the same time",
            ),
            explanationTitle = "The Science Behind It",
            explanationParagraphs = listOf(
                "Alcohol can help you fall asleep...but it will result in poor quality sleep. Even a single drink can have a negative impact on your sleep quality.",
                "Alcohol suppresses your central nervous system and increases the production of adenosine—a chemical that makes you feel sleepy. This can make you feel relaxed and help you fall asleep.",
                "However, after a few hours the adenosine production will drop and your body will begin to remove the alcohol from your system, drawing water from your cells and flushing it out along with essential electrolytes. This loss of fluids, electrolytes, and disruptions to sleep rhythms is coupled with alcohol’s ability to block REM sleep. This means that even though you may fall asleep faster the quality of your sleep will be greatly compromised.",
            ),
        ),
        onCloseClick = onCloseClick,
        onGetStartedClick = onGetStartedClick,
        modifier = modifier,
    )
}

@Composable
fun AvoidLateAlcoholSettingsScreen(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    onDoneClick: () -> Unit = {},
) {
    HabitReminderSettingsScreen(
        content = HabitSettingsContent(
            topBarTitle = "Avoid Late Alcohol",
            heroTitle = "Avoid Late Alcohol",
            heroIconRes = R.drawable.alcohols,
            heroGradientEnd = Color(0xFF541923),
            toggleTitle = "Remind me",
            toggleSubtitle = "to avoid alcohol",
            reminderValue = "4h",
        ),
        onCloseClick = onCloseClick,
        onRemindMeClick = onDoneClick,
        onNotNowClick = onDoneClick,
        modifier = modifier,
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun AvoidLateAlcoholScreenPreview() {
    SleeptrackerTheme {
        AvoidLateAlcoholScreen()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun AvoidLateAlcoholSettingsScreenPreview() {
    SleeptrackerTheme {
        AvoidLateAlcoholSettingsScreen()
    }
}
